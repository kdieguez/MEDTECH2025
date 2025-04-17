from fastapi import APIRouter, HTTPException
from database import obtener_coleccion
from models import ServicioCatalogo
from pymongo.errors import PyMongoError
from datetime import datetime

router = APIRouter(
    prefix="/catalogo",
    tags=["Catálogo de Servicios"]
)

catalogo_coll = obtener_coleccion("catalogo_servicios")


@router.post("/")
async def agregar_servicio_al_catalogo(servicio: ServicioCatalogo):
    try:
        nuevo_servicio = servicio.dict()
        catalogo_coll.insert_one(nuevo_servicio)
        return {"message": "Servicio agregado al catálogo exitosamente"}
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al guardar en MongoDB: {str(e)}")


@router.get("/")
async def listar_servicios_catalogo():
    try:
        servicios = []
        for servicio in catalogo_coll.find():
            servicio["_id"] = str(servicio["_id"])
            servicios.append(servicio)
        return servicios
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al leer el catálogo: {str(e)}")


@router.delete("/{id_subcategoria}")
async def eliminar_servicio_del_catalogo(id_subcategoria: int):
    try:
        result = catalogo_coll.delete_one({"servicio.id_subcategoria": id_subcategoria})
        if result.deleted_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado en el catálogo.")
        return {"message": "Servicio eliminado del catálogo correctamente"}
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al eliminar de MongoDB: {str(e)}")