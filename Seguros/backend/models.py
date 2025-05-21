"""
Modelos Pydantic utilizados en el sistema de seguros.

Incluye modelos para representar servicios hospitalarios, servicios en catálogo
y solicitudes de citas, usados tanto en validaciones como en la lógica de negocio.
"""

from pydantic import BaseModel, Field
from typing import Optional, List
from datetime import datetime

class ServicioBase(BaseModel):
    """
    Representa un servicio hospitalario básico.

    Attributes:
        id_servicio (int): ID del servicio principal.
        nombre_servicio (str): Nombre del servicio.
        descripcion_servicio (str): Descripción del servicio.
        id_subcategoria (int): ID de la subcategoría del servicio.
        nombre_subcategoria (str): Nombre de la subcategoría.
        descripcion_subcategoria (str): Descripción de la subcategoría.
        precio (float): Precio establecido por el hospital.
        id_info_doctor (int): ID del doctor o área que brinda el servicio.
    """
    id_servicio: int
    nombre_servicio: str
    descripcion_servicio: str
    id_subcategoria: int
    nombre_subcategoria: str
    descripcion_subcategoria: str
    precio: float
    id_info_doctor: int

class ServicioCatalogo(BaseModel):
    """
    Modelo para representar un servicio incluido en el catálogo del seguro.

    Attributes:
        servicio (ServicioBase): Información detallada del servicio.
        disponible (bool): Indica si está disponible actualmente.
        tipos_poliza (List[str]): Tipos de póliza que lo cubren.
        fecha_agregado (datetime): Fecha en la que fue agregado al catálogo.
    """
    servicio: ServicioBase
    disponible: bool = True
    tipos_poliza: List[str] = Field(default_factory=list)
    fecha_agregado: datetime = Field(default_factory=datetime.utcnow)

class CitaRequest(BaseModel):
    """
    Modelo para representar una solicitud de cita.

    Attributes:
        fecha_hora (str): Fecha y hora de la cita (en formato ISO).
        hospital (str): Nombre del hospital al que se desea agendar.
        servicio (str): Nombre del servicio solicitado.
        id_afiliado (str): ID del afiliado que agenda la cita.
    """
    fecha_hora: str
    hospital: str
    servicio: str
    id_afiliado: str
