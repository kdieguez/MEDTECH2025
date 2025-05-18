from pydantic import BaseModel, Field
from typing import Optional, List
from datetime import datetime

class ServicioBase(BaseModel):
    id_servicio: int
    nombre_servicio: str
    descripcion_servicio: str
    id_subcategoria: int
    nombre_subcategoria: str
    descripcion_subcategoria: str
    precio: float
    id_info_doctor: int

class ServicioCatalogo(BaseModel):
    servicio: ServicioBase
    disponible: bool = True
    tipos_poliza: List[str] = Field(default_factory=list)
    fecha_agregado: datetime = Field(default_factory=datetime.utcnow)

class CitaRequest(BaseModel):
    fecha_hora: str
    hospital: str
    servicio: str
    id_afiliado: str
