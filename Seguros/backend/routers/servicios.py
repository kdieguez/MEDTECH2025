"""
Módulo de gestión de servicios médicos disponibles en el sistema de seguros.

Permite listar, crear, actualizar y eliminar servicios, incluyendo su descripción,
imagen ilustrativa y subcategorías asociadas.
"""

from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from pymongo.errors import PyMongoError

router = APIRouter(
    prefix="/servicios",
    tags=["Servicios"]
)

servicios_coll = obtener_coleccion("servicios")

@router.get("/")
async def listar_servicios():
    """
    Lista todos los servicios registrados en la colección.

    Returns:
        list: Lista de servicios con campos serializados.
    """
    servicios = []
    for servicio in servicios_coll.find():
        servicio["_id"] = str(servicio["_id"])
        servicios.append(servicio)
    return servicios

@router.post("/")
async def crear_servicio(request: Request):
    """
    Crea un nuevo servicio médico.

    Args:
        request (Request): Solicitud JSON con los campos:
            - nombre (str)
            - descripcion (str)
            - imagen (str)
            - subcategorias (list, opcional)

    Returns:
        dict: Mensaje de éxito y ID del nuevo servicio.

    Raises:
        HTTPException: Si faltan campos o hay un error en la base de datos.
    """
    data = await request.json()

    nombre = data.get("nombre")
    descripcion = data.get("descripcion")
    imagen = data.get("imagen")
    subcategorias = data.get("subcategorias", [])

    if not nombre or not descripcion or not imagen:
        raise HTTPException(status_code=400, detail="Faltan campos requeridos.")

    nuevo_servicio = {
        "nombre": nombre,
        "descripcion": descripcion,
        "imagen": imagen,
        "subcategorias": subcategorias
    }

    try:
        result = servicios_coll.insert_one(nuevo_servicio)
        return {"message": "Servicio creado exitosamente", "id": str(result.inserted_id)}
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la base de datos: {str(e)}")

@router.put("/{id}")
async def actualizar_servicio(id: str, request: Request):
    """
    Actualiza un servicio existente por su ID.

    Args:
        id (str): ID del servicio a modificar.
        request (Request): JSON con los campos a actualizar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el servicio no se encuentra o falla la actualización.
    """
    data = await request.json()

    try:
        result = servicios_coll.update_one(
            {"_id": ObjectId(id)},
            {"$set": data}
        )

        if result.matched_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado")

        return {"message": "Servicio actualizado exitosamente"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la base de datos: {str(e)}")

@router.delete("/{id}")
async def eliminar_servicio(id: str):
    """
    Elimina un servicio por su ID.

    Args:
        id (str): ID del servicio a eliminar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el servicio no existe o ocurre un error.
    """
    try:
        result = servicios_coll.delete_one({"_id": ObjectId(id)})

        if result.deleted_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado")

        return {"message": "Servicio eliminado exitosamente"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la base de datos: {str(e)}")
