from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from passlib.context import CryptContext
from jose import jwt
from datetime import datetime, timedelta
from bson import ObjectId
from services.envio_correos import (
    enviar_email_bienvenida,
    enviar_email_admin,
    obtener_admin_emails
)
from pymongo.errors import PyMongoError

# Configuración de seguridad
SECRET_KEY = "aasdfñlkjasdfñlkjasdfñlkj"
ALGORITHM = "HS256"
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

usuarios_coll = obtener_coleccion("usuarios")
estructura_web_coll = obtener_coleccion("estructura_web")

#Router
router = APIRouter(
    prefix="/autenticacion",
    tags=["Autenticacion"]
)

def hash_contrasena(plain_password: str) -> str:
    return pwd_context.hash(plain_password)

def verificar_contrasena(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)

def crear_token_acceso(data: dict, expires_delta: int = 30):
    """Genera un token JWT con expiración en 'expires_delta' minutos."""
    to_encode = data.copy()
    expire = datetime.utcnow() + timedelta(minutes=expires_delta)
    to_encode.update({"exp": expire})
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt

@router.post("/registro")
async def registrar_usuario(request: Request):
    data = await request.json()

    nombre = data.get("nombre")
    apellido = data.get("apellido")
    correo = data.get("correo")
    contrasena = data.get("contrasena")

    if not all([nombre, apellido, correo, contrasena]):
        raise HTTPException(status_code=400, detail="Faltan datos obligatorios.")

    # Verifica si existe el correo en la bd
    if usuarios_coll.find_one({"correo": correo}):
        raise HTTPException(status_code=400, detail="El correo ya está registrado.")

    hashed_pw = hash_contrasena(contrasena)
    fecha_creacion = datetime.utcnow()

    nuevo_usuario = {
        "nombre": nombre,
        "apellido": apellido,
        "correo": correo,
        "contrasena_hashed": hashed_pw,
        "rol": "",
        "estado": False,
        "fecha_creacion": fecha_creacion
    }

    try:
        resultado = usuarios_coll.insert_one(nuevo_usuario)
        enviar_email_bienvenida(correo, nombre) #envio de correo a usuario nuevo
        admin_emails = obtener_admin_emails() #envio de correo a admins

        if admin_emails:
            datos_usuario = {
                "nombre": nombre,
                "apellido": apellido,
                "correo": correo,
                "fecha_creacion": fecha_creacion.strftime("%d/%m/%Y %H:%M UTC")
            }

            for admin_correo in admin_emails:
                enviar_email_admin(admin_correo, datos_usuario)
        return {
            "message": "Usuario registrado exitosamente",
            "id": str(resultado.inserted_id)
        }

    except PyMongoError as e:
        raise HTTPException(status_code=500, detail=f"Error en la base de datos: {str(e)}")

@router.post("/login")
async def iniciar_sesion(request: Request):
    data = await request.json()
    correo = data.get("correo")
    contrasena = data.get("contrasena")
    
    if not correo or not contrasena:
        raise HTTPException(status_code=400, detail="Correo y contraseña son requeridos.")
    usuario_doc = usuarios_coll.find_one({"correo": correo})

    if not usuario_doc:
        raise HTTPException(status_code=401, detail="Credenciales inválidas")

    if not usuario_doc.get("estado", False):
        raise HTTPException(
            status_code=401,
            detail="Tu cuenta aún no está activa. Espera a que un administrador la habilite."
        )

    if not verificar_contrasena(contrasena, usuario_doc["contrasena_hashed"]):
        raise HTTPException(status_code=401, detail="Credenciales inválidas")

    token_acceso = crear_token_acceso({
        "sub": usuario_doc["correo"],
        "rol": usuario_doc.get("rol", "")
    })

    return {
        "access_token": token_acceso,
        "token_type": "bearer",
        "rol": usuario_doc.get("rol", ""),
        "estado": usuario_doc.get("estado", False)
    }


