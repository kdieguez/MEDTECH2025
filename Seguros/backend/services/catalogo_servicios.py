from pymongo import MongoClient
from backend.models import ServicioCatalogo
from backend.database import get_mongo_database

# Colección
def get_catalogo_collection():
    db = get_mongo_database()
    return db["catalogo_servicios"]

# Insertar un servicio hospitalario en el catálogo del seguro
def agregar_servicio_al_catalogo(servicio_data: ServicioCatalogo):
    collection = get_catalogo_collection()
    result = collection.insert_one(servicio_data.dict())
    return str(result.inserted_id)