"""
Módulo de conexión a la base de datos MongoDB para el sistema de seguros.

Proporciona una función para obtener referencias directas a colecciones específicas
dentro de la base de datos `seguros_db`.
"""

from pymongo import MongoClient

def obtener_coleccion(nombre_coleccion: str):
    """
    Obtiene una colección de la base de datos `seguros_db`.

    Args:
        nombre_coleccion (str): Nombre de la colección que se desea obtener.

    Returns:
        Collection: Referencia a la colección de MongoDB.
    """
    cliente = MongoClient("mongodb://localhost:27017/")
    db = cliente["seguros_db"]
    return db[nombre_coleccion]
