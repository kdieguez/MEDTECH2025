"""
Módulo de acceso a datos para el catálogo de servicios hospitalarios en el sistema de seguros.

Permite insertar servicios hospitalarios en la colección `catalogo_servicios` en MongoDB.
"""

from pymongo import MongoClient
from backend.models import ServicioCatalogo
from backend.database import get_mongo_database

def get_catalogo_collection():
    """
    Obtiene la colección `catalogo_servicios` de la base de datos MongoDB.

    Returns:
        Collection: Referencia a la colección de servicios del catálogo.
    """
    db = get_mongo_database()
    return db["catalogo_servicios"]

def agregar_servicio_al_catalogo(servicio_data: ServicioCatalogo) -> str:
    """
    Inserta un nuevo servicio hospitalario en el catálogo del sistema de seguros.

    Args:
        servicio_data (ServicioCatalogo): Objeto Pydantic con los datos del servicio.

    Returns:
        str: ID del documento insertado en la colección como cadena.
    """
    collection = get_catalogo_collection()
    result = collection.insert_one(servicio_data.dict())
    return str(result.inserted_id)
