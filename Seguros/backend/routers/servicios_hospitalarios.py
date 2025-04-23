from fastapi import APIRouter, HTTPException
import httpx

router = APIRouter()

@router.get("/servicios-hospitalarios")
async def obtener_servicios_hospitalarios():
    try:
        async with httpx.AsyncClient() as client:
            response = await client.get("http://localhost:7000/servicios-hospitalarios")
            response.raise_for_status()
            return response.json()
    except httpx.HTTPStatusError as e:
        raise HTTPException(status_code=e.response.status_code, detail="Error desde hospitales: " + e.response.text)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error general: {str(e)}")
