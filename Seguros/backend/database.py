from pymongo import MongoClient

def obtener_coleccion(nombre_coleccion="estructura_web"):
    cliente = MongoClient("mongodb://localhost:27017/")
    db = cliente["seguros_db"]
    return db[nombre_coleccion]
