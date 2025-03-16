from pydantic import BaseModel
from typing import Optional

class Pagina(BaseModel):
    titulo: str
    contenido: str
    categoria: Optional[str] = None
