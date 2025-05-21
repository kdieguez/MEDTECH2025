"""
Módulo de gestión de citas para el sistema de seguros.

Permite listar, crear, actualizar y eliminar citas, además de obtener hospitales y servicios disponibles.
Coordina las operaciones entre la base de datos local y los backends de hospitales registrados.
"""

from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List
from datetime import datetime
import requests
from database import obtener_coleccion
from bson import ObjectId
import os
from dotenv import load_dotenv

# Cargar variables de entorno
load_dotenv()
BASE_URL = os.getenv("BASE_URL", "http://localhost:8000")

# Inicializar router y colecciones
router = APIRouter()
citas = obtener_coleccion("citas")
hospitales = obtener_coleccion("hospitales")
servicios = obtener_coleccion("servicios")

# Modelos
class ServicioData(BaseModel):
    """
    Datos de un servicio médico.
    """
    id_subcategoria: str
    id_servicio: str
    nombre_subcategoria: str
    nombre_servicio: str

class CitaRequest(BaseModel):
    """
    Datos requeridos para registrar una nueva cita.
    """
    fechaHora: str
    hospital: str
    servicio: ServicioData
    id_afiliado: str

class CitaUpdate(BaseModel):
    """
    Datos para actualizar una cita existente.
    """
    nuevaFechaHora: str
    nuevaSubcategoria: str
    nuevoServicio: str

@router.get("/citas", response_model=List[dict])
def obtener_citas():
    """
    Retorna todas las citas registradas en el sistema de seguros.

    Returns:
        List[dict]: Lista de citas serializadas.
    """
    try:
        lista = []
        for c in citas.find():
            c["_id"] = str(c["_id"])
            lista.append(c)
        return lista
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/citas")
def crear_cita(cita: CitaRequest):
    """
    Crea una nueva cita, sincronizándola con el backend del hospital correspondiente.

    Args:
        cita (CitaRequest): Datos de la cita a registrar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si ocurre un error de validación, conexión o persistencia.
    """
    try:
        yaExiste = citas.find_one({
            "fechaHora": cita.fechaHora,
            "idPaciente": cita.id_afiliado,
            "idSubcategoria": cita.servicio.id_subcategoria,
        })
        if yaExiste:
            raise HTTPException(status_code=400, detail="Ya existe una cita en ese horario para ese hospital y servicio.")

        hospital = hospitales.find_one({"nombre": cita.hospital})
        if not hospital or "url_backend" not in hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend registrado")

        datos_cita_para_hospital = {
            "fechaHora": cita.fechaHora,
            "idAfiliado": cita.id_afiliado,
            "nombreSubcategoria": cita.servicio.nombre_subcategoria
        }

        try:
            response = requests.post(
                f"{hospital['url_backend'].rstrip('/')}/citas/externa",
                json=datos_cita_para_hospital,
                timeout=5
            )
            data = response.json()
            id_hospital = data.get("idCita")

            if response.status_code >= 400:
                raise HTTPException(status_code=502, detail=f"Error al crear cita en hospital: {response.text}")
        except requests.exceptions.RequestException:
            raise HTTPException(status_code=502, detail="No se pudo contactar al hospital")

        try:
            info_paciente = requests.get(
                f"{BASE_URL}/usuarios/paciente/por-afiliado/{cita.id_afiliado}",
                timeout=5
            ).json()
            nombre_paciente = f"{info_paciente.get('nombre', '')} {info_paciente.get('apellido', '')}".strip()
        except Exception:
            nombre_paciente = cita.id_afiliado

        cita_data = {
            "fechaHora": datetime.fromisoformat(cita.fechaHora),
            "idPaciente": cita.id_afiliado,
            "nombrePaciente": nombre_paciente,
            "hospital": cita.hospital,
            "servicio": cita.servicio.nombre_servicio,
            "subcategoria": cita.servicio.nombre_subcategoria,
            "idSubcategoria": cita.servicio.id_subcategoria,
            "idCitaHospital": id_hospital
        }
        citas.insert_one(cita_data)

        return {"mensaje": "Cita registrada correctamente en seguros y hospital"}

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/hospitales", response_model=List[dict])
def obtener_hospitales():
    """
    Lista todos los hospitales activos disponibles en el sistema.

    Returns:
        List[dict]: Lista de hospitales.
    """
    try:
        return list(hospitales.find({"estado": "activo"}, {"_id": 0}))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/servicios", response_model=List[dict])
def obtener_servicios():
    """
    Lista todos los servicios disponibles.

    Returns:
        List[dict]: Lista de servicios.
    """
    try:
        return list(servicios.find({}, {"_id": 0}))
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.delete("/citas/{id}")
def eliminar_cita(id: str):
    """
    Elimina una cita tanto del sistema de seguros como del hospital.

    Args:
        id (str): ID de la cita en MongoDB.

    Returns:
        dict: Mensaje de confirmación.

    Raises:
        HTTPException: Si no se encuentra o no puede comunicarse con el hospital.
    """
    try:
        cita = citas.find_one({"_id": ObjectId(id)})
        if not cita:
            raise HTTPException(status_code=404, detail="Cita no encontrada")

        hospital = hospitales.find_one({"nombre": cita["hospital"]})
        if not hospital or "url_backend" not in hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend")

        id_hospital = cita.get("idCitaHospital")
        if id_hospital:
            try:
                response = requests.delete(
                    f"{hospital['url_backend'].rstrip('/')}/citas/{id_hospital}",
                    timeout=5
                )
                if response.status_code >= 400:
                    raise HTTPException(status_code=502, detail=f"No se pudo eliminar en hospital: {response.text}")
            except requests.exceptions.RequestException:
                raise HTTPException(status_code=502, detail="No se pudo contactar al hospital")

        citas.delete_one({"_id": ObjectId(id)})
        return {"mensaje": "Cita eliminada correctamente en seguros y hospital"}

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.put("/citas/{id}")
def actualizar_cita(id: str, datos: CitaUpdate):
    """
    Actualiza la información de una cita existente tanto en el hospital como en el sistema local.

    Args:
        id (str): ID de la cita en MongoDB.
        datos (CitaUpdate): Nuevos datos para actualizar.

    Returns:
        dict: Mensaje de éxito.

    Raises:
        HTTPException: Si la cita no se encuentra o hay fallos en la comunicación.
    """
    try:
        cita = citas.find_one({"_id": ObjectId(id)})
        if not cita:
            raise HTTPException(status_code=404, detail="Cita no encontrada")

        hospital = hospitales.find_one({"nombre": cita["hospital"]})
        if not hospital or "url_backend" not in hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend")

        id_cita_hospital = cita.get("idCitaHospital")
        if not id_cita_hospital:
            raise HTTPException(status_code=400, detail="Cita no tiene id en hospital")

        try:
            response = requests.put(
                f"{hospital['url_backend'].rstrip('/')}/citas/{id_cita_hospital}",
                json={
                    "nuevaFechaHora": datos.nuevaFechaHora,
                    "nuevaSubcategoria": datos.nuevaSubcategoria
                },
                timeout=5
            )
            if response.status_code >= 400:
                raise HTTPException(status_code=502, detail=f"Error al actualizar en hospital: {response.text}")
        except requests.exceptions.RequestException:
            raise HTTPException(status_code=502, detail="No se pudo contactar al hospital")

        citas.update_one(
            {"_id": ObjectId(id)},
            {
                "$set": {
                    "fechaHora": datetime.fromisoformat(datos.nuevaFechaHora),
                    "subcategoria": datos.nuevaSubcategoria,
                    "servicio": datos.nuevoServicio
                }
            }
        )

        return {"mensaje": "Cita actualizada en seguros y hospital"}

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error inesperado: {str(e)}")
