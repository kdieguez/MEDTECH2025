"""
Módulo de envío de correos electrónicos para el sistema de seguros.

Proporciona funciones para enviar correos de bienvenida, activación, notificación a administradores,
y comentarios sobre la moderación de cambios en la plataforma web.
"""

import smtplib
from email.message import EmailMessage
from database import obtener_coleccion
from datetime import datetime

EMAIL_FROM = "kds2games@gmail.com"
EMAIL_PASSWORD = "ncsa noau lkcd gics"
SMTP_SERVER = "smtp.gmail.com"
SMTP_PORT = 587

def obtener_nombre_seguro() -> str:
    """
    Obtiene el nombre personalizado del seguro desde la configuración del portal.

    Returns:
        str: Nombre del seguro o "Seguros S.A." por defecto.
    """
    coleccion = obtener_coleccion("estructura_web")
    pagina = coleccion.find_one({"titulo": "Header y Footer"})
    if pagina and "nombre_seguro" in pagina:
        return pagina["nombre_seguro"]
    return "Seguros S.A."

def obtener_admin_emails() -> list:
    """
    Obtiene los correos electrónicos de los administradores activos.

    Returns:
        list: Lista de correos electrónicos.
    """
    usuarios = obtener_coleccion("usuarios")
    admins = usuarios.find({"rol": "admin", "estado": True})
    correos = [admin.get("correo") for admin in admins if "correo" in admin]
    return correos if correos else []

def enviar_email_bienvenida(destinatario: str, nombre_usuario: str):
    """
    Envía un correo de bienvenida al nuevo usuario registrado.

    Args:
        destinatario (str): Correo del usuario.
        nombre_usuario (str): Nombre del usuario.
    """
    nombre_seguro = obtener_nombre_seguro()

    msg = EmailMessage()
    msg["Subject"] = f"¡Bienvenido a {nombre_seguro}!"
    msg["From"] = EMAIL_FROM
    msg["To"] = destinatario

    msg.set_content(f"""
    Hola {nombre_usuario},

    ¡Bienvenido a {nombre_seguro}!

    Tu registro ha sido exitoso. Pronto te llegará un correo para que puedas ingresar al sitio web.

    Saludos,
    El equipo de {nombre_seguro}
    """)
    _enviar_email(msg)

def enviar_email_admin(admin_email: str, datos_usuario: dict):
    """
    Notifica a un administrador que un nuevo usuario se ha registrado.

    Args:
        admin_email (str): Correo del administrador.
        datos_usuario (dict): Diccionario con nombre, apellido, correo y fecha_creacion del usuario.
    """
    nombre_seguro = obtener_nombre_seguro()

    msg = EmailMessage()
    msg["Subject"] = f"Nuevo usuario registrado en {nombre_seguro}"
    msg["From"] = EMAIL_FROM
    msg["To"] = admin_email

    contenido = f"""
    Hola Admin,

    Se ha registrado un nuevo usuario en {nombre_seguro}.

    Datos del usuario:
    - Nombre: {datos_usuario['nombre']}
    - Apellido: {datos_usuario['apellido']}
    - Correo: {datos_usuario['correo']}
    - Fecha de creación: {datos_usuario['fecha_creacion']}

    Por favor, revisa el sistema para asignarle un rol y activar su cuenta.
    """
    msg.set_content(contenido)
    _enviar_email(msg)

def enviar_email_activacion(destinatario: str, nombre_usuario: str):
    """
    Envía un correo notificando al usuario que su cuenta ha sido activada.

    Args:
        destinatario (str): Correo del usuario.
        nombre_usuario (str): Nombre del usuario.
    """
    nombre_seguro = obtener_nombre_seguro()

    msg = EmailMessage()
    msg["Subject"] = f"¡Tu cuenta en {nombre_seguro} ha sido activada!"
    msg["From"] = EMAIL_FROM
    msg["To"] = destinatario

    msg.set_content(f"""
    Hola {nombre_usuario},

    ¡Tu cuenta en {nombre_seguro} ha sido activada exitosamente!

    Ahora puedes iniciar sesión y completar tu información en nuestro sistema.

    Ingresa aquí para comenzar: http://localhost:5173/Login

    Saludos,
    El equipo de {nombre_seguro}
    """)
    _enviar_email(msg)

def enviar_email_bienvenida_con_password(destinatario: str, nombre_usuario: str, password: str):
    """
    Envía un correo de bienvenida incluyendo una contraseña temporal.

    Args:
        destinatario (str): Correo del usuario.
        nombre_usuario (str): Nombre del usuario.
        password (str): Contraseña temporal generada.
    """
    nombre_seguro = obtener_nombre_seguro()

    msg = EmailMessage()
    msg["Subject"] = f"¡Bienvenido a {nombre_seguro}!"
    msg["From"] = EMAIL_FROM
    msg["To"] = destinatario

    msg.set_content(f"""
    Hola {nombre_usuario},

    ¡Bienvenido a {nombre_seguro}!

    Tu registro ha sido exitoso. Tu contraseña temporal es: {password}

    Espera a que un administrador active tu cuenta. Luego podrás ingresar al sistema.

    Saludos,
    El equipo de {nombre_seguro}
    """)
    _enviar_email(msg)

def enviar_email_rechazo(destinatario: str, comentario_admin: str):
    """
    Informa al autor que su contenido fue rechazado, incluyendo el comentario del administrador.

    Args:
        destinatario (str): Correo del autor del draft.
        comentario_admin (str): Comentario del administrador.
    """
    nombre_seguro = obtener_nombre_seguro()

    msg = EmailMessage()
    msg["Subject"] = f"Tu cambio fue rechazado en {nombre_seguro}"
    msg["From"] = EMAIL_FROM
    msg["To"] = destinatario

    msg.set_content(f"""
    Hola,

    El cambio que propusiste en el sitio de {nombre_seguro} ha sido rechazado por un administrador.

    Comentario del administrador:
    {comentario_admin or "Sin comentarios."}

    Puedes ingresar de nuevo para editar el contenido y reenviarlo a revisión.

    Saludos,
    El equipo de {nombre_seguro}
    """)
    _enviar_email(msg)

def _enviar_email(msg: EmailMessage):
    """
    Función auxiliar para enviar un mensaje de correo usando SMTP.

    Args:
        msg (EmailMessage): Mensaje ya construido.
    """
    try:
        with smtplib.SMTP(SMTP_SERVER, SMTP_PORT) as smtp:
            smtp.starttls()
            smtp.login(EMAIL_FROM, EMAIL_PASSWORD)
            smtp.send_message(msg)
        print(f"Correo enviado a {msg['To']}")
    except Exception as e:
        print(f"Error al enviar el correo a {msg['To']}: {e}")
