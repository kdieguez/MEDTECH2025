"""
Módulo de gestión de hospitales afiliados al sistema de seguros.

Permite listar, crear, actualizar y desactivar hospitales. Los hospitales activos
pueden ser utilizados para registrar citas y mostrar información al usuario.
"""

from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from pymongo.errors import PyMongoError

router = APIRouter(
    prefix="/hospitales",
    tags=["Hospitales Afiliados"]
)

hospitales_coll = obtener_coleccion("hospitales")

@router.get("/")
async def listar_hospitales():
    """
    Lista todos los hospitales que están marcados como activos.

    Returns:
        list: Lista de hospitales con campos completos y `_id` serializado.
    """
    hospitales = []
    for hospital in hospitales_coll.find({"estado": "activo"}):
        hospital["_id"] = str(hospital["_id"])
        hospitales.append(hospital)
    return hospitales

@router.post("/")
async def crear_hospital(request: Request):
    """
    Crea un nuevo hospital afiliado.

    Args:
        request (Request): Cuerpo de la solicitud con los campos:
            - nombre
            - direccion
            - telefono
            - email (opcional)
            - imagen (opcional)
            - servicios (opcional)

    Returns:
        dict: Mensaje de confirmación y ID del hospital creado.

    Raises:
        HTTPException: Si faltan campos obligatorios o hay error en la base de datos.
    """
    data = await request.json()

    nombre = data.get("nombre")
    direccion = data.get("direccion")
    telefono = data.get("telefono")
    email = data.get("email")
    imagen = data.get("imagen")
    servicios = data.get("servicios", [])

    if not nombre or not direccion or not telefono:
        raise HTTPException(status_code=400, detail="Faltan campos obligatorios")

    nuevo_hospital = {
        "nombre": nombre,
        "direccion": direccion,
        "telefono": telefono,
        "email": email,
        "imagen": imagen,
        "servicios": servicios,
        "estado": "activo"
    }

    try:
        result = hospitales_coll.insert_one(nuevo_hospital)
        return {"message": "Hospital creado", "id": str(result.inserted_id)}
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la BD: {str(e)}")

@router.put("/{id}")
async def actualizar_hospital(id: str, request: Request):
    """
    Actualiza los datos de un hospital existente.

    Args:
        id (str): ID del hospital a actualizar.
        request (Request): JSON con los campos que se desean actualizar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el hospital no se encuentra o ocurre un error en la base de datos.
    """
    data = await request.json()

    try:
        result = hospitales_coll.update_one(
            {"_id": ObjectId(id)},
            {"$set": data}
        )

        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Hospital no encontrado")

        return {"message": "Hospital actualizado"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la BD: {str(e)}")

@router.delete("/{id}")
async def eliminar_hospital(id: str):
    """
    Desactiva lógicamente un hospital (sin eliminarlo físicamente).

    Args:
        id (str): ID del hospital a desactivar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el hospital no existe o hay error en la base de datos.
    """
    try:
        result = hospitales_coll.update_one(
            {"_id": ObjectId(id)},
            {"$set": {"estado": "inactivo"}}
        )

        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Hospital no encontrado")

        return {"message": "Hospital desactivado"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la BD: {str(e)}")
