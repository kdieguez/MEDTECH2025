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
    hospitales = []
    for hospital in hospitales_coll.find({"estado": "activo"}):
        hospital["_id"] = str(hospital["_id"])
        hospitales.append(hospital)
    return hospitales

@router.post("/")
async def crear_hospital(request: Request):
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
