from fastapi import APIRouter, HTTPException
from pydantic import BaseModel
from typing import List
from datetime import datetime
import requests
from database import obtener_coleccion
from bson import ObjectId

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

class CitaUpdate(BaseModel):
    nuevaFechaHora: str
    nuevaSubcategoria: str
    nuevoServicio: str

# ---------- Endpoints ----------

@router.get("/citas", response_model=List[dict])
def obtener_citas():
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

            data = response.json()
            id_hospital = data.get("idCita")

            if response.status_code >= 400:
                raise HTTPException(status_code=502, detail=f"Error al crear cita en hospital: {response.text}")
        except requests.exceptions.RequestException:
            raise HTTPException(status_code=502, detail="No se pudo contactar al hospital")

        # Obtener nombre completo del paciente desde backend hospital
        try:
            info_paciente = requests.get(
                f"http://localhost:8000/usuarios/paciente/por-afiliado/{cita.id_afiliado}",
                timeout=5
            ).json()

            nombre_paciente = f"{info_paciente.get('nombre', '')} {info_paciente.get('apellido', '')}".strip()
        except Exception:
            nombre_paciente = cita.id_afiliado  # fallback si no responde

        # Guardar en Mongo local
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

@router.delete("/citas/{id}")
def eliminar_cita(id: str):
    try:
        # Obtener la cita por su _id de Mongo
        cita = citas.find_one({"_id": ObjectId(id)})
        if not cita:
            raise HTTPException(status_code=404, detail="Cita no encontrada")

        # Obtener URL del backend del hospital
        hospital = hospitales.find_one({"nombre": cita["hospital"]})
        if not hospital or "url_backend" not in hospital:
            raise HTTPException(status_code=404, detail="Hospital no encontrado o sin backend")

        # Enviar solicitud DELETE al backend del hospital
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
        else:
            print("No hay idCitaHospital en Mongo. Solo se eliminará localmente.")

        # Eliminar de Mongo
        citas.delete_one({"_id": ObjectId(id)})

        return {"mensaje": "Cita eliminada correctamente en seguros y hospital"}

    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.put("/citas/{id}")
def actualizar_cita(id: str, datos: CitaUpdate):
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

        # Actualizar en hospital
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

        # Actualizar también el servicio en Mongo
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
