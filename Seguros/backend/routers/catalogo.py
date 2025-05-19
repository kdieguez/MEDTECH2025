from fastapi import APIRouter, HTTPException
from database import obtener_coleccion
from models import ServicioCatalogo
from pymongo.errors import PyMongoError

router = APIRouter(
    prefix="/catalogo",
    tags=["Catálogo de Servicios"]
)

catalogo_coll = obtener_coleccion("catalogo_servicios")
servicios_coll = obtener_coleccion("servicios")


@router.post("/")
async def agregar_servicio_al_catalogo(servicio: ServicioCatalogo):
    try:
        nuevo_servicio = servicio.dict()
        id_sub = nuevo_servicio["servicio"]["id_subcategoria"]

        # Validar que no esté ya en el catálogo
        if catalogo_coll.find_one({"servicio.id_subcategoria": id_sub}):
            raise HTTPException(status_code=400, detail="El servicio ya está en el catálogo.")

        #Insertar en catálogo
        catalogo_coll.insert_one(nuevo_servicio)

        #Verificar si ya existe en `servicios`
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
        # Eliminar del catálogo
        result_catalogo = catalogo_coll.delete_one({"servicio.id_subcategoria": id_subcategoria})
        if result_catalogo.deleted_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado en el catálogo.")

        # También eliminar de servicios (si existe)
        servicios_coll.delete_one({"id_subcategoria": id_subcategoria})

        return {"message": "Servicio eliminado del catálogo y de la colección de servicios"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error al eliminar de MongoDB: {str(e)}")
