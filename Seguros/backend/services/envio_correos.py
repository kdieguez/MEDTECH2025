import smtplib
from email.message import EmailMessage
from database import obtener_coleccion
from datetime import datetime

EMAIL_FROM = "kds2games@gmail.com"
EMAIL_PASSWORD = "ncsa noau lkcd gics"
SMTP_SERVER = "smtp.gmail.com"
SMTP_PORT = 587

def obtener_nombre_seguro():
    coleccion = obtener_coleccion("estructura_web")
    pagina = coleccion.find_one({"titulo": "Header y Footer"})
    if pagina and "nombre_seguro" in pagina:
        return pagina["nombre_seguro"]
    return "Seguros S.A."

def obtener_admin_emails():
    """Obtiene todos los correos de administradores activos"""
    usuarios = obtener_coleccion("usuarios")
    admins = usuarios.find({"rol": "admin", "estado": True})  # solo activos

    correos = [admin.get("correo") for admin in admins if "correo" in admin]

    return correos if correos else []

def enviar_email_bienvenida(destinatario, nombre_usuario):
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

def enviar_email_admin(admin_email, datos_usuario):
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

def _enviar_email(msg):
    try:
        with smtplib.SMTP(SMTP_SERVER, SMTP_PORT) as smtp:
            smtp.starttls()
            smtp.login(EMAIL_FROM, EMAIL_PASSWORD)
            smtp.send_message(msg)

        print(f"Correo enviado a {msg['To']}")
    except Exception as e:
        print(f"Error al enviar el correo a {msg['To']}: {e}")

def enviar_email_activacion(destinatario, nombre_usuario):
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
