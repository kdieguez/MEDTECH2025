"""
Archivo principal del backend del sistema de seguros.

Inicializa la aplicación FastAPI, configura CORS y registra los routers
que manejan los distintos módulos del sistema: usuarios, autenticación,
estructura web, servicios, ventas, hospitales, cobros, etc.
"""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers import (
    estructura_web,
    autenticacion,
    usuarios,
    servicios,
    hospitales,
    ventas,
    cobros,
    servicios_hospitalarios,
    catalogo,
    citas
)

# Inicialización de la aplicación FastAPI
app = FastAPI()

# Configuración de CORS
origins = [
    "http://localhost:5173",  # Frontend local
    "*"  # En producción, reemplazar por dominios específicos
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Registro de routers
app.include_router(estructura_web.router)
app.include_router(autenticacion.router)
app.include_router(usuarios.router)
app.include_router(servicios.router)
app.include_router(hospitales.router)
app.include_router(ventas.router)
app.include_router(cobros.router)
app.include_router(servicios_hospitalarios.router)
app.include_router(catalogo.router)
app.include_router(citas.router)

@app.get("/")
def read_root():
    """
    Ruta de prueba para verificar que el backend está funcionando.

    Returns:
        dict: Mensaje de confirmación.
    """
    return {"message": "El backend funciona"}
