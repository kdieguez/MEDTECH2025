from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from services.envio_correos import enviar_email_activacion

router = APIRouter(
    prefix="/usuarios",
    tags=["Usuarios"]
)

usuarios_coll = obtener_coleccion("usuarios")

@router.get("/listar")
async def listar_usuarios():
    """
    Endpoint para obtener la lista de usuarios registrados.
    """
    usuarios = list(usuarios_coll.find({}, {"contrasena_hashed": 0}))
    for usuario in usuarios:
        usuario["_id"] = str(usuario["_id"])
    return {"usuarios": usuarios}

@router.put("/actualizar-estado/{usuario_id}")
async def actualizar_estado_usuario(usuario_id: str, request: Request):
    """
    Endpoint para activar/desactivar usuarios.
    """
    data = await request.json()
    nuevo_estado = data.get("estado")

    if nuevo_estado is None:
        raise HTTPException(status_code=400, detail="El estado es obligatorio.")

    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {"$set": {"estado": nuevo_estado}}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="Usuario no encontrado o sin cambios.")

    return {"message": "Estado del usuario actualizado correctamente"}

@router.put("/asignar-rol/{usuario_id}")
async def asignar_rol(usuario_id: str, request: Request):
    """
    Endpoint para asignar un rol a un usuario.
    """
    data = await request.json()
    nuevo_rol = data.get("rol")

    if not nuevo_rol:
        raise HTTPException(status_code=400, detail="El rol es obligatorio.")

    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {"$set": {"rol": nuevo_rol}}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="Usuario no encontrado o sin cambios.")

    return {"message": "Rol asignado correctamente"}

@router.put("/editar/{usuario_id}")
async def editar_usuario(usuario_id: str, request: Request):
    """
    Endpoint para editar la información completa de un usuario.
    Si el estado cambia a activo, se envía un correo de activación.
    """
    data = await request.json()

    nombre = data.get("nombre")
    apellido = data.get("apellido")
    correo = data.get("correo")
    rol = data.get("rol")
    estado = data.get("estado")
    permiso_editar_paginas = data.get("permiso_editar_paginas", False)

    if not all([nombre, apellido, correo, rol]):
        raise HTTPException(status_code=400, detail="Faltan datos obligatorios.")

    usuario_actual = usuarios_coll.find_one({"_id": ObjectId(usuario_id)})

    if not usuario_actual:
        raise HTTPException(status_code=404, detail="Usuario no encontrado.")

    enviar_correo_activacion = False
    if not usuario_actual.get("estado", False) and estado:
        enviar_correo_activacion = True

    resultado = usuarios_coll.update_one(
        {"_id": ObjectId(usuario_id)},
        {"$set": {
            "nombre": nombre,
            "apellido": apellido,
            "correo": correo,
            "rol": rol,
            "estado": estado,
            "permiso_editar_paginas": permiso_editar_paginas
        }}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=404, detail="Usuario no encontrado o sin cambios.")

    if enviar_correo_activacion:
        enviar_email_activacion(correo, nombre)

    return {"message": "Usuario editado correctamente"}
