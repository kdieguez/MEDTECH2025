"""
Módulo de proxy para obtener los servicios hospitalarios desde el sistema de hospitales.

Este endpoint actúa como intermediario para consultar servicios médicos ofrecidos por hospitales
desde el sistema de seguros.
"""

from fastapi import APIRouter, HTTPException
import httpx

router = APIRouter()

URL_BACKEND_HOSPITAL = "http://localhost:7000"

@router.get("/servicios-hospitalarios")
async def obtener_servicios_hospitalarios():
    """
    Consulta los servicios hospitalarios disponibles en el sistema de hospitales.

    Returns:
        list: Lista de servicios hospitalarios disponibles.

    Raises:
        HTTPException: Si hay un error HTTP desde el hospital o falla general de conexión.
    """
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get(f"{URL_BACKEND_HOSPITAL}/servicios-hospitalarios")
            response.raise_for_status()
            return response.json()
    except httpx.HTTPStatusError as e:
        raise HTTPException(status_code=e.response.status_code, detail="Error desde hospitales: " + e.response.text)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error general: {str(e)}")
