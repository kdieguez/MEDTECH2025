"""
Módulo de Cobros para el sistema de seguros.

Permite listar usuarios con pólizas activas, renovar pólizas con validación de tarjeta,
y deshabilitar usuarios manualmente si no han renovado.
"""

from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from datetime import datetime, timedelta

router = APIRouter(
    prefix="/cobros",
    tags=["Cobros"]
)

usuarios_coll = obtener_coleccion("usuarios")

@router.get("/listar")
async def listar_para_cobros():
    """
    Lista usuarios activos con rol 'paciente' ordenados por fecha de vencimiento de póliza.

    Returns:
        list: Usuarios con nombre, apellido y fecha de vencimiento.
    """
    usuarios = list(
        usuarios_coll.find(
            {"rol": "paciente", "estado": True},
            {"nombre": 1, "apellido": 1, "fecha_vencimiento": 1}
        ).sort("fecha_vencimiento", 1)
    )
    for usuario in usuarios:
        usuario["_id"] = str(usuario["_id"])
    return usuarios

def validar_tarjeta_luhn(numero: str) -> bool:
    """
    Valida un número de tarjeta usando el algoritmo de Luhn.

    Args:
        numero (str): Número de tarjeta como string.

    Returns:
        bool: True si el número es válido, False en caso contrario.
    """
    total = 0
    reverso = numero[::-1]

    for i, digito in enumerate(reverso):
        n = int(digito)
        if i % 2 == 1:
            n *= 2
            if n > 9:
                n -= 9
        total += n

    return total % 10 == 0

@router.put("/renovar/{usuario_id}")
async def renovar_poliza(usuario_id: str, request: Request):
    """
    Renueva la póliza de un usuario, actualizando la fecha de vencimiento
    y guardando el número de tarjeta si pasa la validación Luhn.

    Args:
        usuario_id (str): ID del usuario en MongoDB.
        request (Request): Cuerpo de la solicitud con los campos `nueva_fecha`, `numero_tarjeta`, `cvv`.

    Returns:
        dict: Mensaje de confirmación.

    Raises:
        HTTPException: Si faltan datos, la tarjeta es inválida o no se encuentra el usuario.
    """
    data = await request.json()
    nueva_fecha = data.get("nueva_fecha")
    numero_tarjeta = data.get("numero_tarjeta")
    cvv = data.get("cvv")

    if not nueva_fecha or not numero_tarjeta:
        raise HTTPException(status_code=400, detail="Faltan datos obligatorios.")

    if not validar_tarjeta_luhn(numero_tarjeta):
        raise HTTPException(status_code=400, detail="Número de tarjeta inválido.")
    
    if not cvv.isdigit() or not (3 <= len(cvv) <= 4):
        raise HTTPException(status_code=400, detail="CVV inválido. Debe tener 3 dígitos.")

    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {
            "$set": {
                "fecha_vencimiento": nueva_fecha,
                "numero_tarjeta": numero_tarjeta
            }
        }
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="No se actualizó el usuario.")

    return {"message": "Póliza renovada exitosamente."}

@router.put("/deshabilitar/{usuario_id}")
async def deshabilitar_usuario(usuario_id: str):
    """
    Deshabilita manualmente un usuario, marcando su campo `estado` como `False`.

    Args:
        usuario_id (str): ID del usuario en MongoDB.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el usuario no se encuentra.
    """
    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {"$set": {"estado": False}}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="No se encontró el usuario.")

    return {"message": "Usuario deshabilitado correctamente."}
