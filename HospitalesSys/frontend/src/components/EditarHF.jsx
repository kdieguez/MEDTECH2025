import React, { useEffect, useState } from 'react';
import './css/editarHF.css';
import axios from 'axios';

const EditarHeaderFooter = () => {
  const [campos, setCampos] = useState([]);
  const [mensaje, setMensaje] = useState('');

  useEffect(() => {
    axios.get('http://localhost:7000/header-footer')
      .then(res => setCampos(res.data))
      .catch(() => setMensaje('Error al cargar datos'));
  }, []);

  const handleChange = (id, campo, valor) => {
    setCampos(prev =>
      prev.map(item =>
        item.idHf === id ? { ...item, [campo]: valor } : item
      )
    );
  };

  const guardarCampo = (campo) => {
    axios.put(`http://localhost:7000/header-footer/${campo.idHf}`, campo)
      .then(() => setMensaje('Guardado correctamente'))
      .catch(() => setMensaje('Error al guardar'));
  };

  const header = campos.filter(c => c.titulo === "NOMBRE_PAGINA" || c.titulo === "LOGO");
  const footer = campos.filter(c => c.titulo !== "NOMBRE_PAGINA" && c.titulo !== "LOGO");

  return (
    <div className="editar-container">
      <h2>Editar Header</h2>
      <div className="editar-seccion">
        {header.map(campo => (
          <div key={campo.idHf} className="campo">
            <label>{campo.titulo === "LOGO" ? "URL del Logo" : "Nombre de la Página"}</label>
            <input
              type="text"
              value={campo.contenido}
              onChange={e => handleChange(campo.idHf, 'contenido', e.target.value)}
            />
            <button onClick={() => guardarCampo(campo)}>Guardar</button>
          </div>
        ))}
      </div>

      <h2>Editar Footer</h2>
      <div className="editar-seccion">
        {footer.map(campo => (
          <div key={campo.idHf} className="campo">
            <label>Título</label>
            <input
              type="text"
              value={campo.titulo}
              onChange={e => handleChange(campo.idHf, 'titulo', e.target.value)}
            />
            <label>Contenido</label>
            <input
              type="text"
              value={campo.contenido}
              onChange={e => handleChange(campo.idHf, 'contenido', e.target.value)}
            />
            <button onClick={() => guardarCampo(campo)}>Guardar</button>
          </div>
        ))}
      </div>

      {mensaje && <p className="mensaje">{mensaje}</p>}
    </div>
  );
};

export default EditarHeaderFooter;
