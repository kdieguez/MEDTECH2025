import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './css/formulariocita.css'; 

export default function FormularioCita() {
  const { idCita } = useParams();
  const navigate = useNavigate();

  const [diagnostico, setDiagnostico] = useState('');
  const [siguientesPasos, setSiguientesPasos] = useState('');
  const [resultadosExamenes, setResultadosExamenes] = useState(['']);
  const [citaId, setCitaId] = useState(null);

  useEffect(() => {
    if (idCita) {
      setCitaId(Number(idCita));
    }
  }, [idCita]);

  const agregarResultado = () => {
    setResultadosExamenes([...resultadosExamenes, '']);
  };

  const handleResultadoChange = (index, value) => {
    const copia = [...resultadosExamenes];
    copia[index] = value;
    setResultadosExamenes(copia);
  };

  const enviarFormulario = () => {
    if (!citaId) {
      alert('Error: No se encontró ID de la cita.');
      return;
    }

    const payload = {
      idCita: citaId,
      diagnostico,
      pasosSiguientes: siguientesPasos,
      urlsResultadosExamenes: resultadosExamenes.filter(url => url.trim() !== ''),
      crearRecetaMedica: false
    };

    axios.post('http://localhost:7000/formulario-cita', payload, {
      headers: { 'Content-Type': 'application/json' }
    })
    .then(() => {
      alert('¡Formulario enviado exitosamente!');
      navigate('/consultarCitas');
    })
    .catch(err => {
      console.error('Error al enviar el formulario', err);
      alert('Hubo un error al guardar el formulario.');
    });
  };

  const irACrearReceta = () => {
    navigate(`/crearReceta/${citaId}`);
  };

  return (
    <div className="finalizar-cita-container">
      <h2>Formulario de Cita</h2>

      <div className="formulario-finalizar">
        <label>Diagnóstico:</label>
        <textarea
          value={diagnostico}
          onChange={(e) => setDiagnostico(e.target.value)}
        />

        <label>Siguientes Pasos:</label>
        <textarea
          value={siguientesPasos}
          onChange={(e) => setSiguientesPasos(e.target.value)}
        />

        <label>Resultados de Exámenes (URL):</label>
        {resultadosExamenes.map((resultado, index) => (
          <input
            key={index}
            type="text"
            placeholder="URL del resultado de examen"
            value={resultado}
            onChange={(e) => handleResultadoChange(index, e.target.value)}
          />
        ))}
        <button type="button" onClick={agregarResultado}>Agregar otro resultado</button>

        <button type="button" className="btn-finalizar" onClick={enviarFormulario}>
          Guardar Formulario
        </button>

        {/* ✅ Botón para crear receta */}
        <button type="button" className="btn-receta" onClick={irACrearReceta}>
          Crear Receta Médica
        </button>
      </div>
    </div>
  );
}
