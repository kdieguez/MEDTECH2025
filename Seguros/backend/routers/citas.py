from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List
from datetime import datetime
import requests
from database import obtener_coleccion

router = APIRouter()

citas = obtener_coleccion("citas")
hospitales = obtener_coleccion("hospitales")
servicios = obtener_coleccion("servicios")

# ---------- Modelos ----------

class ServicioData(BaseModel):
    id_subcategoria: str
    id_servicio: str
    nombre_subcategoria: str
    nombre_servicio: str

class CitaRequest(BaseModel):
    fechaHora: str  # Formato ISO: "2025-05-20T08:00:00"
    hospital: str   # Solo se usa para saber a qué backend enviar
    servicio: ServicioData
    id_afiliado: str  # Código de afiliación

# ---------- Endpoints ----------

@router.get("/citas", response_model=List[dict])
def obtener_citas():
    try:
        return list(citas.find({}, {"_id": 0}))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.post("/citas")
def crear_cita(cita: CitaRequest):
    try:
        # Verificar duplicado en Mongo
        yaExiste = citas.find_one({
            "fechaHora": cita.fechaHora,
            "idPaciente": cita.id_afiliado,
            "idSubcategoria": cita.servicio.id_subcategoria,
        })
        if yaExiste:
            raise HTTPException(status_code=400, detail="Ya existe una cita en ese horario para ese hospital y servicio.")

        # Buscar hospital y su backend
        hospital = hospitales.find_one({"nombre": cita.hospital})
        if not hospital or "url_backend" not in hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend registrado")

        # Preparar JSON para enviar al hospital
        datos_cita_para_hospital = {
            "fechaHora": cita.fechaHora,
            "idAfiliado": cita.id_afiliado,
            "nombreSubcategoria": cita.servicio.nombre_subcategoria
        }

        # Enviar al backend del hospital
        try:
            print(f"➡ Enviando al hospital la subcategoría: [{cita.servicio.nombre_subcategoria}]")
            response = requests.post(
    f"{hospital['url_backend'].rstrip('/')}/citas/externa",
    json=datos_cita_para_hospital,
    timeout=5
)

            if response.status_code >= 400:
                raise HTTPException(status_code=502, detail=f"Error al crear cita en hospital: {response.text}")
        except requests.exceptions.RequestException:
            raise HTTPException(status_code=502, detail="No se pudo contactar al hospital")

        # Guardar en Mongo local
        cita_data = {
            "fechaHora": datetime.fromisoformat(cita.fechaHora),
            "idPaciente": cita.id_afiliado,
            "idSubcategoria": cita.servicio.id_subcategoria,
            "hospital": cita.hospital
        }
        citas.insert_one(cita_data)

        return {"mensaje": "Cita registrada correctamente en seguros y hospital"}

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/hospitales", response_model=List[dict])
def obtener_hospitales():
    try:
        return list(hospitales.find({"estado": "activo"}, {"_id": 0}))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@router.get("/servicios", response_model=List[dict])
def obtener_servicios():
    try:
        return list(servicios.find({}, {"_id": 0}))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
