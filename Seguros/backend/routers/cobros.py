from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from datetime import datetime, timedelta

router = APIRouter(
    prefix="/cobros",
    tags=["Cobros"]
)

usuarios_coll = obtener_coleccion("usuarios")

# Obtener usuarios por fecha de vencimiento
@router.get("/listar")
async def listar_para_cobros():
    usuarios = list(
        usuarios_coll.find(
            {"rol": "paciente", "estado": True},
            {"nombre": 1, "apellido": 1, "fecha_vencimiento": 1}
        ).sort("fecha_vencimiento", 1)
    )
    for usuario in usuarios:
        usuario["_id"] = str(usuario["_id"])
    return usuarios

# Renovar póliza
def validar_tarjeta_luhn(numero):
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
                "numero_tarjeta": numero_tarjeta  # Se guarda no enmascarado
            }
        }
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="No se actualizó el usuario.")

    return {"message": "Póliza renovada exitosamente."}

# Deshabilitar usuario
@router.put("/deshabilitar/{usuario_id}")
async def deshabilitar_usuario(usuario_id: str):
    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {"$set": {"estado": False}}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="No se encontró el usuario.")

    return {"message": "Usuario deshabilitado correctamente."}