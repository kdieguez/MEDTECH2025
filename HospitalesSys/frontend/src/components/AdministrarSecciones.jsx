import React, { useEffect, useState, useCallback } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './css/adminsecciones.css';

function AdministrarSecciones() {
  const navigate = useNavigate();
  const [paginas, setPaginas] = useState([]);
  const [paginaSeleccionada, setPaginaSeleccionada] = useState('');
  const [secciones, setSecciones] = useState([]);
  const [autorizado, setAutorizado] = useState(null);

  const [nuevaSeccion, setNuevaSeccion] = useState({
    titulo: '',
    contenido: '',
    imagenUrl: '',
    orden: 1,
  });

  const [modoEdicion, setModoEdicion] = useState(null);
  const [seccionEditada, setSeccionEditada] = useState({
    titulo: '',
    contenido: '',
    imagenUrl: '',
    orden: 1,
  });

  useEffect(() => {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const { rol } = JSON.parse(datosUsuario);
      setAutorizado(rol === 1 || rol === 2);
    } else {
      setAutorizado(false);
    }
  }, []);

  useEffect(() => {
    if (autorizado) {
      axios.get('http://localhost:7000/paginas')
        .then(response => setPaginas(response.data))
        .catch(error => console.error('Error al cargar páginas:', error));
    }
  }, [autorizado]);

  const cargarSecciones = useCallback(() => {
    if (paginaSeleccionada) {
      axios.get(`http://localhost:7000/paginas/${paginaSeleccionada}/secciones`)
        .then(response => setSecciones(response.data))
        .catch(error => console.error('Error al cargar secciones:', error));
    } else {
      setSecciones([]);
    }
  }, [paginaSeleccionada]);

  useEffect(() => {
    cargarSecciones();
  }, [paginaSeleccionada, cargarSecciones]);

  const manejarCambioNuevaSeccion = (e) => {
    const { name, value } = e.target;
    setNuevaSeccion(prev => ({ ...prev, [name]: value }));
  };

  const manejarCambioEditarSeccion = (e) => {
    const { name, value } = e.target;
    setSeccionEditada(prev => ({ ...prev, [name]: value }));
  };

  const agregarSeccion = () => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));

    const propuesta = {
      titulo: nuevaSeccion.titulo,
      contenido: nuevaSeccion.contenido,
      imagenUrl: nuevaSeccion.imagenUrl,
      orden: nuevaSeccion.orden,
      tipo: "CREACION",
      pagina: { id: parseInt(paginaSeleccionada) },
      usuarioSolicitante: { id: usuario.id }
    };

    axios.post("http://localhost:7000/cambios", propuesta)
      .then(() => {
        alert("Sección propuesta para aprobación");
        setNuevaSeccion({ titulo: '', contenido: '', imagenUrl: '', orden: 1 });
      })
      .catch(error => {
        console.error("Error al proponer sección:", error);
        alert("Error al proponer sección.");
      });
  };

  const eliminarSeccion = (idSeccion) => {
    if (!window.confirm('¿Seguro que deseas eliminar esta sección?')) return;
    axios.delete(`http://localhost:7000/secciones/${idSeccion}`)
      .then(() => {
        alert('Sección eliminada exitosamente');
        setSecciones(secciones.filter(sec => sec.id !== idSeccion));
      })
      .catch(error => {
        console.error('Error al eliminar sección:', error);
        alert('Error al eliminar sección.');
      });
  };

  const iniciarEdicion = (seccion) => {
    setModoEdicion(seccion.id);
    setSeccionEditada({
      titulo: seccion.titulo,
      contenido: seccion.contenido,
      imagenUrl: seccion.imagenUrl,
      orden: seccion.orden,
    });
  };

  const guardarEdicion = (idSeccion) => {
    const usuario = JSON.parse(localStorage.getItem("usuario"));

    const propuesta = {
      titulo: seccionEditada.titulo,
      contenido: seccionEditada.contenido,
      imagenUrl: seccionEditada.imagenUrl,
      orden: seccionEditada.orden,
      tipo: "EDICION",
      pagina: { id: parseInt(paginaSeleccionada) },
      usuarioSolicitante: { id: usuario.id },
      idSeccionOriginal: idSeccion
    };

    axios.post("http://localhost:7000/cambios", propuesta)
      .then(() => {
        alert("Cambio enviado para aprobación");
        setModoEdicion(null);
        cargarSecciones();
      })
      .catch(error => {
        console.error("Error al enviar cambio:", error);
        alert("Error al enviar cambio.");
      });
  };

  if (autorizado === null) {
    return <p>Cargando...</p>;
  }

  if (!autorizado) {
    return (
      <div className="denegado">
        <h2>🚫 Acceso Denegado</h2>
        <p>No tienes permiso para administrar secciones.</p>
        <button onClick={() => navigate('/')}>Volver al inicio</button>
      </div>
    );
  }

  // 🔍 Filtro para mostrar solo secciones únicas
  const seccionesUnicas = secciones.reduce((acc, actual) => {
    const yaExiste = acc.find(sec =>
      sec.titulo === actual.titulo &&
      sec.contenido === actual.contenido &&
      sec.imagenUrl === actual.imagenUrl &&
      sec.orden === actual.orden
    );
    if (!yaExiste) acc.push(actual);
    return acc;
  }, []);

  return (
    <div className="admin-secciones-container">
      <h2>Administrar Secciones del Hospital</h2>

      <label>Seleccionar Página:</label>
      <select value={paginaSeleccionada} onChange={e => setPaginaSeleccionada(e.target.value)}>
        <option value="">-- Seleccionar Página --</option>
        {paginas.length > 0 ? (
          paginas.map(pagina => (
            <option key={pagina.id} value={pagina.id}>{pagina.nombre}</option>
          ))
        ) : (
          <option disabled>No hay páginas disponibles</option>
        )}
      </select>

      {paginaSeleccionada && (
        <>
          <h3>Agregar Nueva Sección</h3>
          <input
            name="titulo"
            placeholder="Título"
            value={nuevaSeccion.titulo}
            onChange={manejarCambioNuevaSeccion}
          />
          <textarea
            name="contenido"
            placeholder="Contenido HTML"
            value={nuevaSeccion.contenido}
            onChange={manejarCambioNuevaSeccion}
          />
          <input
            name="imagenUrl"
            placeholder="URL de Imagen (opcional)"
            value={nuevaSeccion.imagenUrl}
            onChange={manejarCambioNuevaSeccion}
          />
          <input
            type="number"
            name="orden"
            placeholder="Orden"
            value={nuevaSeccion.orden}
            onChange={manejarCambioNuevaSeccion}
          />
          <button onClick={agregarSeccion}>Agregar Sección</button>

          <h3>Secciones Actuales</h3>
          {seccionesUnicas.length > 0 ? (
            seccionesUnicas.map(seccion => (
              <div key={seccion.id} className="seccion-item">
                {modoEdicion === seccion.id ? (
                  <>
                    <input
                      name="titulo"
                      value={seccionEditada.titulo}
                      onChange={manejarCambioEditarSeccion}
                    />
                    <textarea
                      name="contenido"
                      value={seccionEditada.contenido}
                      onChange={manejarCambioEditarSeccion}
                    />
                    <input
                      name="imagenUrl"
                      value={seccionEditada.imagenUrl}
                      onChange={manejarCambioEditarSeccion}
                    />
                    <input
                      type="number"
                      name="orden"
                      value={seccionEditada.orden}
                      onChange={manejarCambioEditarSeccion}
                    />
                    <button onClick={() => guardarEdicion(seccion.id)}>Enviar Cambio</button>
                    <button onClick={() => setModoEdicion(null)}>Cancelar</button>
                  </>
                ) : (
                  <>
                    <h4>{seccion.titulo}</h4>
                    <button onClick={() => iniciarEdicion(seccion)}>Editar</button>
                    <button onClick={() => eliminarSeccion(seccion.id)}>Eliminar</button>
                  </>
                )}
              </div>
            ))
          ) : (
            <p>No hay secciones registradas para esta página.</p>
          )}
        </>
      )}
    </div>
  );
}

export default AdministrarSecciones;