"""
Módulo de estructura web para el sistema de seguros.

Permite gestionar el contenido editable del sitio web, incluyendo header, footer,
contenido general, y un sistema de drafts (borradores) que requiere revisión y moderación.
"""

from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId
from datetime import datetime
from services.envio_correos import enviar_email_rechazo

router = APIRouter(
    prefix="/estructura_web",
    tags=["EstructuraWeb"]
)

coleccion = obtener_coleccion("estructura_web")
coleccion_drafts = obtener_coleccion("estructura_drafts")

@router.get("/")
def obtener_paginas():
    """
    Retorna todas las páginas disponibles en la colección de estructura web.

    Returns:
        list: Lista de páginas con sus campos y `_id` en string.
    """
    paginas = list(coleccion.find())
    for p in paginas:
        p["_id"] = str(p["_id"])
    return paginas

@router.get("/por-id/{id_pagina}")
def obtener_pagina_por_id(id_pagina: str):
    """
    Retorna una página específica por su ID.

    Args:
        id_pagina (str): ID de la página.

    Returns:
        dict: Documento de la página encontrada.

    Raises:
        HTTPException: Si el ID es inválido o la página no existe.
    """
    try:
        obj_id = ObjectId(id_pagina)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    pagina = coleccion.find_one({"_id": obj_id})
    if not pagina:
        raise HTTPException(status_code=404, detail="Página no encontrada")

    pagina["_id"] = str(pagina["_id"])
    return pagina

@router.put("/actualizar-header-footer/{id_pagina}")
async def actualizar_header_footer(id_pagina: str, request: Request):
    """
    Actualiza los campos de header y footer de una página.

    Args:
        id_pagina (str): ID de la página.
        request (Request): Datos JSON con los campos: nombre_seguro, logo, footer.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el ID es inválido o no se modificó la página.
    """
    try:
        obj_id = ObjectId(id_pagina)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    data = await request.json()
    campos_validos = {"nombre_seguro", "logo", "footer"}
    datos_filtrados = {k: v for k, v in data.items() if k in campos_validos}

    if not datos_filtrados:
        raise HTTPException(status_code=400, detail="Faltan datos para Header y Footer.")

    resultado = coleccion.update_one(
        {"_id": obj_id},
        {"$set": datos_filtrados}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=400, detail="No se actualizó la página")

    return {"message": "Header y Footer actualizado correctamente"}

@router.put("/actualizar-contenido/{id_pagina}")
async def actualizar_contenido(id_pagina: str, request: Request):
    """
    Actualiza campos de contenido dinámico de una página.

    Args:
        id_pagina (str): ID de la página.
        request (Request): Contenido JSON con campos permitidos.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si no se reciben campos válidos o no se modifica la página.
    """
    try:
        obj_id = ObjectId(id_pagina)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    data = await request.json()
    campos_permitidos = {
        "contenido", "secciones", "banner", "titulo", "carrusel",
        "porcentaje", "descripcion", "servicios"
    }
    datos_filtrados = {k: v for k, v in data.items() if k in campos_permitidos}

    if not datos_filtrados:
        raise HTTPException(status_code=400, detail="No se recibió ningún dato válido para actualizar.")

    resultado = coleccion.update_one(
        {"_id": obj_id},
        {"$set": datos_filtrados}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=400, detail="No se actualizó la página")

    return {"message": "Contenido actualizado correctamente"}

@router.post("/guardar-draft/{id_pagina}")
async def guardar_draft(id_pagina: str, request: Request):
    """
    Guarda un borrador de contenido pendiente de aprobación.

    Args:
        id_pagina (str): ID de la página.
        request (Request): JSON con campos permitidos y opcionalmente `autor`.

    Returns:
        dict: Mensaje de éxito.
    """
    try:
        obj_id = ObjectId(id_pagina)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    data = await request.json()
    campos_permitidos = {
        "contenido", "secciones", "banner", "titulo", "carrusel",
        "porcentaje", "descripcion", "servicios"
    }
    draft_data = {k: v for k, v in data.items() if k in campos_permitidos}

    if not draft_data:
        raise HTTPException(status_code=400, detail="No se recibió contenido válido para el draft.")

    nuevo_draft = {
        "id_pagina": str(obj_id),
        "contenido": draft_data,
        "estado": "pendiente",
        "fecha": datetime.utcnow(),
        "comentario_admin": "",
        "autor": data.get("autor", "sistema")
    }

    coleccion_drafts.insert_one(nuevo_draft)

    return {"message": "Cambios guardados como borrador para revisión"}

@router.get("/drafts-pendientes")
def obtener_drafts_pendientes():
    """
    Lista todos los drafts que están pendientes de revisión.

    Returns:
        list: Lista de drafts pendientes.
    """
    drafts = list(coleccion_drafts.find({"estado": "pendiente"}))
    for d in drafts:
        d["_id"] = str(d["_id"])
    return drafts

@router.get("/draft/{id_draft}")
def obtener_draft(id_draft: str):
    """
    Retorna un draft específico por su ID.

    Args:
        id_draft (str): ID del draft.

    Returns:
        dict: Documento del draft.

    Raises:
        HTTPException: Si el ID es inválido o el draft no existe.
    """
    try:
        draft_id = ObjectId(id_draft)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    draft = coleccion_drafts.find_one({"_id": draft_id})
    if not draft:
        raise HTTPException(status_code=404, detail="Draft no encontrado")

    draft["_id"] = str(draft["_id"])
    return draft

@router.post("/moderar/{id_draft}")
async def moderar_draft(id_draft: str, request: Request):
    """
    Permite moderar un draft, aprobándolo o rechazándolo con comentarios.

    Args:
        id_draft (str): ID del draft.
        request (Request): JSON con los campos:
            - accion: 'aprobar' o 'rechazar'
            - comentario: comentario del administrador en caso de rechazo.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si el ID es inválido, el draft no existe o la acción no es válida.
    """
    try:
        draft_id = ObjectId(id_draft)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    data = await request.json()
    accion = data.get("accion")
    comentario = data.get("comentario", "")

    draft = coleccion_drafts.find_one({"_id": draft_id})
    if not draft:
        raise HTTPException(status_code=404, detail="Draft no encontrado")

    id_pagina = draft["id_pagina"]
    contenido = draft["contenido"]

    if accion == "aprobar":
        coleccion.update_one({"_id": ObjectId(id_pagina)}, {"$set": contenido})
        coleccion_drafts.update_one({"_id": draft_id}, {"$set": {"estado": "aprobado"}})
        return {"message": "Cambios aprobados y aplicados correctamente."}

    elif accion == "rechazar":
        coleccion_drafts.update_one(
            {"_id": draft_id},
            {"$set": {"estado": "rechazado", "comentario_admin": comentario}}
        )
        autor = draft.get("autor", None)
        if autor and autor != "sistema":
            enviar_email_rechazo(autor, comentario)

        return {"message": "Draft rechazado con comentario y notificación al autor."}

    else:
        raise HTTPException(status_code=400, detail="Acción inválida: usar 'aprobar' o 'rechazar'")
