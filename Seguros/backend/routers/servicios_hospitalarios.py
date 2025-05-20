from fastapi import APIRouter, HTTPException, Query
import httpx
from database import obtener_coleccion  # asegúrate de tener esto

router = APIRouter()

hospitales = obtener_coleccion("hospitales")

@router.get("/servicios-hospitalarios")
async def obtener_servicios_hospitalarios(hospital: str = Query(..., description="Nombre del hospital")):
    try:
        # Buscar la URL del hospital en Mongo
        info_hospital = hospitales.find_one({"nombre": hospital})
        if not info_hospital or "url_backend" not in info_hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend configurado")

        url = info_hospital["url_backend"].rstrip("/")

        async with httpx.AsyncClient() as client:
            response = await client.get(f"{url}/servicios-hospitalarios")
            response.raise_for_status()
            return response.json()
    except httpx.HTTPStatusError as e:
        raise HTTPException(status_code=e.response.status_code, detail="Error desde hospitales: " + e.response.text)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error general: {str(e)}")
