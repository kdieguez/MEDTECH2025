from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from pymongo.errors import PyMongoError

router = APIRouter(
    prefix="/servicios",
    tags=["Servicios"]
)

servicios_coll = obtener_coleccion("servicios")

# Obtener todos los servicios
@router.get("/")
async def listar_servicios():
    servicios = []
    for servicio in servicios_coll.find():
        servicio["_id"] = str(servicio["_id"])
        servicios.append(servicio)
    return servicios

# Crear un nuevo servicio
@router.post("/")
async def crear_servicio(request: Request):
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

# Editar un servicio existente
@router.put("/{id}")
async def actualizar_servicio(id: str, request: Request):
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

# Eliminar un servicio
@router.delete("/{id}")
async def eliminar_servicio(id: str):
    try:
        result = servicios_coll.delete_one({"_id": ObjectId(id)})

        if result.deleted_count == 0:
            raise HTTPException(status_code=404, detail="Servicio no encontrado")

        return {"message": "Servicio eliminado exitosamente"}

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la base de datos: {str(e)}")
