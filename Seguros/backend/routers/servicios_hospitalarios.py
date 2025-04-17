from fastapi import APIRouter
import oracledb

router = APIRouter()

DB_USER = "Hospital_Base"
DB_PASSWORD = "Hospital"
DB_DSN = "localhost/XEPDB1"

@router.get("/servicios-hospitalarios")
def obtener_servicios_hospitalarios():
    try:
        conn = oracledb.connect(user=DB_USER, password=DB_PASSWORD, dsn=DB_DSN)
        cursor = conn.cursor()

        query = """
        SELECT 
            sh.ID_SERVICIO,
            sh.NOMBRE_SERVICIO,
            sh.DESCRIPCION AS DESCRIPCION_SERVICIO,
            sc.ID_SUBCATEGORIA,
            sc.NOMBRE_SUBCATEGORIA,
            sc.DESCRIPCION AS DESCRIPCION_SUBCATEGORIA,
            sc.PRECIO,
            LISTAGG(sd.ID_INFO_DOCTOR, ',') WITHIN GROUP (ORDER BY sd.ID_INFO_DOCTOR) AS IDS_DOCTORES
        FROM SERVICIOS_HOSPITALARIOS sh
        LEFT JOIN SUBCATEGORIAS_SERVICIO sc ON sh.ID_SERVICIO = sc.ID_SERVICIO
        LEFT JOIN SERVICIO_X_DOCTOR sd ON sh.ID_SERVICIO = sd.ID_SERVICIO
        GROUP BY 
            sh.ID_SERVICIO,
            sh.NOMBRE_SERVICIO,
            sh.DESCRIPCION,
            sc.ID_SUBCATEGORIA,
            sc.NOMBRE_SUBCATEGORIA,
            sc.DESCRIPCION,
            sc.PRECIO
        ORDER BY sh.ID_SERVICIO, sc.ID_SUBCATEGORIA
        """

        cursor.execute(query)
        columnas = [col[0] for col in cursor.description]
        resultados = cursor.fetchall()

        servicios = []
        for fila in resultados:
            fila_dict = dict(zip(columnas, fila))
            if fila_dict["IDS_DOCTORES"]:
                fila_dict["ID_INFO_DOCTOR"] = list(map(int, fila_dict["IDS_DOCTORES"].split(",")))
            else:
                fila_dict["ID_INFO_DOCTOR"] = []
            del fila_dict["IDS_DOCTORES"]
            servicios.append(fila_dict)

        return {"servicios": servicios}

    except Exception as e:
        return {"error": str(e)}
    
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()