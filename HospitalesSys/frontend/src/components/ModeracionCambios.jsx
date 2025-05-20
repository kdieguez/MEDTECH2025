import { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";

function ModeracionCambios() {
  const [cambios, setCambios] = useState([]);
  const [comentariosRechazo, setComentariosRechazo] = useState({});
  const [esAdmin, setEsAdmin] = useState(false);

    const usuario = JSON.parse(localStorage.getItem("usuario"));
    const idAprobador = usuario?.id;

  useEffect(() => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));
    setEsAdmin(usuario?.rol === 1);
    cargarCambiosPendientes();
  }, []);

  const cargarCambiosPendientes = () => {
    axios.get("http://localhost:7000/cambios/pendientes")
      .then(res => setCambios(res.data))
      .catch(() => Swal.fire("Error", "No se pudieron cargar los cambios", "error"));
  };

  const aprobarCambio = (id) => {
    axios.put(`http://localhost:7000/cambios/aprobar/${id}?aprobador=${idAprobador}`)
      .then(() => {
        Swal.fire("Aprobado", "El cambio fue aprobado exitosamente", "success");
        cargarCambiosPendientes();
      })
      .catch(() => Swal.fire("Error", "No se pudo aprobar", "error"));
  };

  const rechazarCambio = (id) => {
    const comentario = comentariosRechazo[id];
    if (!comentario) {
      Swal.fire("Falta comentario", "Debes escribir un motivo del rechazo", "warning");
      return;
    }

    axios.put(`http://localhost:7000/cambios/rechazar/${id}?aprobador=${idAprobador}&comentario=${encodeURIComponent(comentario)}`)
      .then(() => {
        Swal.fire("Rechazado", "El cambio fue rechazado exitosamente", "success");
        cargarCambiosPendientes();
      })
      .catch(() => Swal.fire("Error", "No se pudo rechazar", "error"));
  };

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Moderación de Cambios</h2>

      {!esAdmin ? (
        <p className="text-red-500 font-semibold">🚫 No tienes permiso para moderar cambios.</p>
      ) : (
        cambios.length === 0 ? (
          <p>No hay cambios pendientes.</p>
        ) : (
          cambios.map(cambio => (
            <div key={cambio.id} className="border p-4 mb-4 rounded shadow">
              <h3 className="text-xl font-semibold">Página: {cambio.pagina?.nombre}</h3>
              <p><strong>Título:</strong> {cambio.titulo}</p>
              <p><strong>Contenido:</strong> {cambio.contenido}</p>
              <p><strong>Solicitante:</strong> {cambio.usuarioSolicitante?.nombre} {cambio.usuarioSolicitante?.apellido}</p>
              {cambio.imagenUrl && (
                <img src={cambio.imagenUrl} alt="Imagen propuesta" className="max-w-xs my-2 border" />
              )}
              <div className="flex flex-col mt-3">
                <textarea
                  placeholder="Motivo de rechazo (si aplica)"
                  className="border rounded p-2 mb-2"
                  value={comentariosRechazo[cambio.id] || ""}
                  onChange={(e) =>
                    setComentariosRechazo({ ...comentariosRechazo, [cambio.id]: e.target.value })
                  }
                />
                <div className="flex gap-3">
                  <button
                    onClick={() => aprobarCambio(cambio.id)}
                    className="bg-green-500 text-white px-4 py-1 rounded"
                  >
                    Aprobar
                  </button>
                  <button
                    onClick={() => rechazarCambio(cambio.id)}
                    className="bg-red-500 text-white px-4 py-1 rounded"
                  >
                    Rechazar
                  </button>
                </div>
              </div>
            </div>
          ))
        )
      )}
    </div>
  );
}

export default ModeracionCambios;