import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/especialidadesCombo.css';

const EspecialidadesCombo = ({ onEspecialidadSeleccionada }) => {
  const [especialidades, setEspecialidades] = useState([]);
  const [nueva, setNueva] = useState('');
  const [seleccionada, setSeleccionada] = useState('');

  useEffect(() => {
    axios.get("http://localhost:7000/especialidades")
      .then(res => setEspecialidades(res.data))
      .catch(err => console.error("Error al cargar especialidades", err));
  }, []);

  const agregarNueva = () => {
    if (!nueva.trim()) return;

    axios.post("http://localhost:7000/especialidades", { nombre: nueva })
      .then((res) => {
        const nuevaEspecialidad = res.data;
        setEspecialidades([...especialidades, nuevaEspecialidad]);
        setSeleccionada(nuevaEspecialidad.id);
        onEspecialidadSeleccionada(nuevaEspecialidad.id); 
        setNueva('');
      })
      .catch(err => {
        console.error("Error al agregar especialidad:", err);
        alert("Error al agregar especialidad.");
      });
  };

  const handleChange = (e) => {
    const idSeleccionado = e.target.value;
    setSeleccionada(idSeleccionado);
    onEspecialidadSeleccionada(Number(idSeleccionado)); // 
  };

  return (
    <div>
      <label>Especialidad:</label>
      <select value={seleccionada} onChange={handleChange}>
        <option value="">Selecciona una especialidad</option>
        {especialidades.map((esp, index) => (
          <option key={index} value={esp.id}>
            {esp.nombre}
          </option>
        ))}
      </select>

      <div style={{ marginTop: '1rem' }}>
        <input
          type="text"
          placeholder="Nueva especialidad"
          value={nueva}
          onChange={(e) => setNueva(e.target.value)}
        />
        <button onClick={agregarNueva}>Agregar</button>
      </div>
    </div>
  );
};

export default EspecialidadesCombo;
