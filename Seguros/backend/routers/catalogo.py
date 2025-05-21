"""
Módulo de Catálogo de Servicios para el sistema de seguros.

Permite agregar, listar y eliminar servicios del catálogo central.
Si un servicio no existe en la colección `servicios`, se agrega automáticamente.
"""

from fastapi import APIRouter, HTTPException
from database import obtener_coleccion
from models import ServicioCatalogo
from pymongo.errors import PyMongoError

# Configuración del router
router = APIRouter(
    prefix="/catalogo",
    tags=["Catálogo de Servicios"]
)

# Colecciones MongoDB
catalogo_coll = obtener_coleccion("catalogo_servicios")
servicios_coll = obtener_coleccion("servicios")

@router.post("/")
async def agregar_servicio_al_catalogo(servicio: ServicioCatalogo):
    """
    Agrega un nuevo servicio al catálogo general. Si no existe en la colección `servicios`,
    también se registra ahí.

    Args:
        servicio (ServicioCatalogo): Objeto que contiene los datos del servicio.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el servicio ya existe o hay errores en la base de datos.
    """
    try:
        nuevo_servicio = servicio.dict()
        id_sub = nuevo_servicio["servicio"]["id_subcategoria"]

        if catalogo_coll.find_one({"servicio.id_subcategoria": id_sub}):
            raise HTTPException(status_code=400, detail="El servicio ya está en el catálogo.")

        catalogo_coll.insert_one(nuevo_servicio)

        if not servicios_coll.find_one({"id_subcategoria": id_sub}):
            datos_servicio = nuevo_servicio["servicio"]
            servicios_coll.insert_one({
                "id_servicio": datos_servicio.get("id_servicio"),
                "nombre_servicio": datos_servicio.get("nombre_servicio"),
                "descripcion_servicio": datos_servicio.get("descripcion_servicio"),
                "id_subcategoria": datos_servicio.get("id_subcategoria"),
                "nombre_subcategoria": datos_servicio.get("nombre_subcategoria"),
                "descripcion_subcategoria": datos_servicio.get("descripcion_subcategoria"),
                "precio": datos_servicio.get("precio"),
                "id_info_doctor": datos_servicio.get("id_info_doctor")
            })

        return {"message": "Servicio agregado al catálogo y también registrado en servicios"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en MongoDB: {str(e)}")

@router.get("/")
async def listar_servicios_catalogo():
    """
    Retorna todos los servicios registrados en el catálogo.

    Returns:
        list: Lista de servicios con sus datos.

    Raises:
        HTTPException: Si ocurre un error al consultar la base de datos.
    """
    try:
        servicios = []
        for servicio in catalogo_coll.find():
            servicio["_id"] = str(servicio["_id"])  # Serializar ObjectId a string
            servicios.append(servicio)
        return servicios
    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al leer el catálogo: {str(e)}")

@router.delete("/{id_subcategoria}")
async def eliminar_servicio_del_catalogo(id_subcategoria: int):
    """
    Elimina un servicio tanto del catálogo como de la colección de servicios.

    Args:
        id_subcategoria (int): ID de la subcategoría del servicio a eliminar.

    Returns:
        dict: Mensaje de confirmación.

    Raises:
        HTTPException: Si el servicio no se encuentra o hay errores en la base de datos.
    """
    try:
        result_catalogo = catalogo_coll.delete_one({"servicio.id_subcategoria": id_subcategoria})
        if result_catalogo.deleted_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado en el catálogo.")

        servicios_coll.delete_one({"id_subcategoria": id_subcategoria})

        return {"message": "Servicio eliminado del catálogo y de la colección de servicios"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al eliminar de MongoDB: {str(e)}")
