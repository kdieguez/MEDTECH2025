from fastapi import APIRouter, HTTPException, Request
from database import obtener_coleccion
from bson import ObjectId

router = APIRouter(
    prefix="/estructura_web",
    tags=["EstructuraWeb"]
)

coleccion = obtener_coleccion("estructura_web")

@router.get("/")
def obtener_paginas():
    paginas = list(coleccion.find())
    for p in paginas:
        p["_id"] = str(p["_id"]) 
    return paginas

@router.get("/por-id/{id_pagina}")
def obtener_pagina_por_id(id_pagina: str):
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
    try:
        obj_id = ObjectId(id_pagina)
    except Exception:
        raise HTTPException(status_code=400, detail="ID inválido")

    data = await request.json()

    if "contenido" not in data:
        raise HTTPException(status_code=400, detail="El contenido es obligatorio en esta página.")

    resultado = coleccion.update_one(
        {"_id": obj_id},
        {"$set": {"contenido": data["contenido"]}}
    )

    if resultado.modified_count == 0:
        raise HTTPException(status_code=400, detail="No se actualizó la página")

    return {"message": "Contenido actualizado correctamente"}
