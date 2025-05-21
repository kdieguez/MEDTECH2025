"""
Utilidad para generación de contraseñas seguras.

Este módulo contiene una función para generar contraseñas aleatorias
usando letras y números.
"""

import random
import string

def generar_contrasena(longitud=10) -> str:
    """
    Genera una contraseña aleatoria alfanumérica.

    Args:
        longitud (int, optional): Longitud de la contraseña. Por defecto es 10.

    Returns:
        str: Contraseña generada.
    """
    caracteres = string.ascii_letters + string.digits
    return ''.join(random.choices(caracteres, k=longitud))
