"""
Módulo de registro de ventas para el sistema de seguros.

Permite registrar un nuevo cliente desde la venta telefónica o presencial,
generando su usuario con contraseña aleatoria, enviando correos de bienvenida
y notificando a los administradores.
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel, EmailStr
from datetime import datetime
from routers.autenticacion import hash_contrasena

from database import obtener_coleccion
from utils.security import generar_contrasena
from services.envio_correos import (
    enviar_email_bienvenida_con_password,
    enviar_email_admin,
    obtener_admin_emails,
)

router = APIRouter()

class VentaInput(BaseModel):
    """
    Modelo de entrada para registrar una venta.

    Attributes:
        nombre (str): Nombre del cliente.
        apellido (str): Apellido del cliente.
        email (EmailStr): Correo electrónico válido.
        tipoPoliza (str): Tipo de póliza seleccionada (ej. "90%", "70%").
    """
    nombre: str
    apellido: str
    email: EmailStr
    tipoPoliza: str

@router.post("/api/ventas")
async def registrar_venta(data: VentaInput):
    """
    Registra un nuevo usuario cliente desde la venta de pólizas.

    Args:
        data (VentaInput): Datos del cliente capturados en la venta.

    Returns:
        dict: Mensaje de confirmación.

    Raises:
        HTTPException: Si el correo ya está registrado.
    """
    contrasena = generar_contrasena()
    fecha_creacion = datetime.utcnow()

    usuarios = obtener_coleccion("usuarios")
    existe = usuarios.find_one({"correo": data.email})
    if existe:
        raise HTTPException(status_code=400, detail="El correo ya está registrado.")

    nuevo_usuario = {
        "nombre": data.nombre,
        "apellido": data.apellido,
        "correo": data.email,
        "contrasena_hashed": hash_contrasena(contrasena),
        "fecha_creacion": fecha_creacion,
        "estado": False,
        "rol": "",
        "tipo_poliza": data.tipoPoliza,
    }

    usuarios.insert_one(nuevo_usuario)

    # Enviar correo de bienvenida con contraseña
    enviar_email_bienvenida_con_password(data.email, data.nombre, contrasena)

    # Notificar a todos los administradores
    for admin_email in obtener_admin_emails():
        enviar_email_admin(admin_email, {
            "nombre": data.nombre,
            "apellido": data.apellido,
            "correo": data.email,
            "fecha_creacion": fecha_creacion,
        })

    return {"mensaje": "Usuario registrado y correos enviados correctamente."}
