import random
import string

def generar_contrasena(longitud=10):
    caracteres = string.ascii_letters + string.digits
    return ''.join(random.choices(caracteres, k=longitud))
