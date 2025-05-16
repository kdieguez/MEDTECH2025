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
  const [imagenes, setImagenes] = useState([]);
  const [imagenAmpliada, setImagenAmpliada] = useState(null);
  const [soloLectura, setSoloLectura] = useState(false);
  const [yaTieneReceta, setYaTieneReceta] = useState(false);

  useEffect(() => {
    if (idCita) {
      axios.get(`http://localhost:7000/formulario-cita/verificar/${idCita}`)
        .then(res => {
          setSoloLectura(res.data.formularioLlenado);
          if (res.data.formularioLlenado) {
            axios.get(`http://localhost:7000/formulario-cita/${idCita}`)
              .then(response => {
                setDiagnostico(response.data.diagnostico || '');
                setSiguientesPasos(response.data.pasosSiguientes || '');
              });          
            axios.get(`http://localhost:7000/formulario-cita/imagenes/${idCita}`)
              .then(response => setImagenes(response.data));
          }
        });

      axios.get(`http://localhost:7000/receta/${idCita}`)
        .then(response => {
          if (response.data?.codigoReceta) {
            setYaTieneReceta(true);
          }
        })
        .catch(() => {
          setYaTieneReceta(false);
        });
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
    if (!idCita) {
      alert('Error: No se encontró ID de la cita.');
      return;
    }

    const payload = {
      idCita: Number(idCita),
      diagnostico,
      pasosSiguientes: siguientesPasos,
      urlsResultadosExamenes: resultadosExamenes.filter(url => url.trim() !== ''),
      crearRecetaMedica: false
    };

    axios.post('http://localhost:7000/formulario-cita', payload)
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
    navigate(`/crearReceta/${idCita}`);
  };

  return (
    <div className="finalizar-cita-container">
      <h2>Formulario de Cita</h2>

      <div className="formulario-finalizar">
        <label>Diagnóstico:</label>
        {soloLectura ? (
          <p>{diagnostico}</p>
        ) : (
          <textarea value={diagnostico} onChange={(e) => setDiagnostico(e.target.value)} />
        )}

        <label>Siguientes Pasos:</label>
        {soloLectura ? (
          <p>{siguientesPasos}</p>
        ) : (
          <textarea value={siguientesPasos} onChange={(e) => setSiguientesPasos(e.target.value)} />
        )}

        <label>Resultados de Exámenes:</label>
        {soloLectura ? (
          <div className="galeria-mini">
            {imagenes.map((img, i) => (
              <img
                key={i}
                src={img.url}
                alt="Resultado"
                className="imagen-mini"
                onClick={() => setImagenAmpliada(img.url)}
              />
            ))}
          </div>
        ) : (
          <>
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
          </>
        )}

        {!soloLectura && (
          <button type="button" className="btn-finalizar" onClick={enviarFormulario}>
            Guardar Formulario
          </button>
        )}

        {yaTieneReceta ? (
          <button type="button" className="btn-receta" onClick={() => navigate(`/crearReceta/${idCita}`)}>
            Ver Receta Médica
          </button>
        ) : (!soloLectura && (
          <button type="button" className="btn-receta" onClick={irACrearReceta}>
            Crear Receta Médica
          </button>
        ))}

        <button type="button" onClick={() => navigate('/consultarCitas')}>
          Volver
        </button>
      </div>

      {imagenAmpliada && (
        <div className="modal-imagen" onClick={() => setImagenAmpliada(null)}>
          <img src={imagenAmpliada} alt="Resultado ampliado" />
        </div>
      )}
    </div>
  );
}
