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
    nombre: str
    apellido: str
    email: EmailStr
    tipoPoliza: str

@router.post("/api/ventas")
async def registrar_venta(data: VentaInput):
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

    enviar_email_bienvenida_con_password(data.email, data.nombre, contrasena)

    for admin_email in obtener_admin_emails():
        enviar_email_admin(admin_email, {
            "nombre": data.nombre,
            "apellido": data.apellido,
            "correo": data.email,
            "fecha_creacion": fecha_creacion,
        })

    return {"mensaje": "Usuario registrado y correos enviados correctamente."}