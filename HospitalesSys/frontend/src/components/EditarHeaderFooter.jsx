import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/editarHF.css'; // Si usarás CSS personalizado

const EditarHeaderFooter = () => {
  const [campos, setCampos] = useState([]);

  useEffect(() => {
    axios.get('/api/header-footer/all').then(res => setCampos(res.data));
  }, []);

  const handleChange = (index, field, value) => {
    const nuevosCampos = [...campos];
    nuevosCampos[index][field] = value;
    setCampos(nuevosCampos);
  };

  const guardarCambios = (campo) => {
    axios.put('/api/header-footer', campo)
      .then(() => alert('¡Campo actualizado correctamente!'))
      .catch(() => alert('Error al guardar el campo.'));
  };

  return (
    <div className="editar-container">
      <h2>Editar Header y Footer</h2>
      <div className="campos-lista">
        {campos.map((campo, index) => (
          <div key={campo.id} className="campo-card">
            <label>ID: {campo.id}</label>
            <input
              type="text"
              value={campo.titulo}
              onChange={e => handleChange(index, 'titulo', e.target.value)}
              placeholder="Título"
            />
            <input
              type="text"
              value={campo.contenido}
              onChange={e => handleChange(index, 'contenido', e.target.value)}
              placeholder="Contenido"
            />
            <button onClick={() => guardarCambios(campo)}>Guardar</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EditarHeaderFooter;
