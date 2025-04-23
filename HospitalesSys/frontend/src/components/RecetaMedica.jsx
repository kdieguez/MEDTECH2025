import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './css/recetamedica.css'; 
import './css/modalmedicamento.css'; 

export default function RecetaMedica() {
  const { id } = useParams();
  const [receta, setReceta] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const [nuevoMedicamento, setNuevoMedicamento] = useState({
    nombre: '',
    principioActivo: '',
    concentracion: '',
    presentacion: '',
    formaFarmaceutica: ''
  });

  const [medicamentosDisponibles, setMedicamentosDisponibles] = useState([]);
  const [medicamentoSeleccionado, setMedicamentoSeleccionado] = useState('');
  const [dosis, setDosis] = useState('');
  const [frecuencia, setFrecuencia] = useState('');
  const [duracion, setDuracion] = useState('');
  const [medicamentosRecetados, setMedicamentosRecetados] = useState([]);

  useEffect(() => {
    axios.get(`http://localhost:7000/receta/${id}`)
      .then(response => setReceta(response.data))
      .catch(err => {
        console.error('Error al cargar receta', err);
        alert('No se pudo cargar la receta');
      });

    axios.get('http://localhost:7000/medicamentos')
      .then(response => setMedicamentosDisponibles(response.data))
      .catch(err => console.error('Error al cargar medicamentos disponibles', err));
  }, [id]);

  const abrirModal = () => setShowModal(true);
  const cerrarModal = () => {
    setShowModal(false);
    setNuevoMedicamento({
      nombre: '',
      principioActivo: '',
      concentracion: '',
      presentacion: '',
      formaFarmaceutica: ''
    });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNuevoMedicamento(prev => ({ ...prev, [name]: value }));
  };

  const guardarMedicamento = () => {
    axios.post('http://localhost:7000/medicamentos', nuevoMedicamento)
      .then(() => {
        alert('Medicamento agregado correctamente');
        cerrarModal();
        axios.get('http://localhost:7000/medicamentos') // 🔥 Volvemos a cargar el listado actualizado
          .then(response => setMedicamentosDisponibles(response.data));
      })
      .catch(err => {
        console.error('Error al guardar medicamento', err);
        alert('Hubo un error al agregar el medicamento');
      });
  };

  const agregarMedicamentoRecetado = () => {
    if (!medicamentoSeleccionado || !dosis || !frecuencia || !duracion) {
      alert('Completa todos los campos para agregar el medicamento.');
      return;
    }

    const nuevo = {
      nombre: medicamentoSeleccionado,
      dosis,
      frecuencia,
      duracion
    };

    setMedicamentosRecetados([...medicamentosRecetados, nuevo]);
    setMedicamentoSeleccionado('');
    setDosis('');
    setFrecuencia('');
    setDuracion('');
  };

  const guardarRecetaCompleta = () => {
    axios.post(`http://localhost:7000/receta/${id}/guardar-medicamentos`, medicamentosRecetados)
      .then(() => {
        alert('Receta médica guardada exitosamente.');
      })
      .catch(err => {
        console.error('Error al guardar receta', err);
        alert('No se pudo guardar la receta.');
      });
  };

  if (!receta) {
    return <div className="receta-container">Cargando receta médica...</div>;
  }

  return (
    <div className="receta-container">
      <div className="encabezado-receta">
        <h1>Hospital La Paz</h1>
        <h3>Receta Médica</h3>
      </div>

      <div className="info-receta">
        <p><strong>Fecha:</strong> {receta.fechaCita}</p>
        <p><strong>Código de Receta:</strong> {receta.codigoReceta}</p>
      </div>

      <div className="paciente-receta">
        <p><strong>Paciente:</strong> {receta.nombrePaciente}</p>
        <p><strong>Médico:</strong> {receta.nombreDoctor}</p>
        <p><strong>No. de Colegiado:</strong> {receta.numColegiado}</p>
        <p><strong>Especialidad:</strong> {receta.especialidad}</p>
      </div>

      {/* 🔵 Botón para agregar nuevo medicamento */}
      <button className="btn-agregar-medicamento" onClick={abrirModal}>
        Agregar nuevo medicamento
      </button>

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Nuevo Medicamento</h2>

            <input type="text" name="nombre" placeholder="Nombre" value={nuevoMedicamento.nombre} onChange={handleInputChange} />
            <input type="text" name="principioActivo" placeholder="Principio Activo" value={nuevoMedicamento.principioActivo} onChange={handleInputChange} />
            <input type="text" name="concentracion" placeholder="Concentración" value={nuevoMedicamento.concentracion} onChange={handleInputChange} />
            <input type="text" name="presentacion" placeholder="Presentación" value={nuevoMedicamento.presentacion} onChange={handleInputChange} />
            <input type="text" name="formaFarmaceutica" placeholder="Forma Farmacéutica" value={nuevoMedicamento.formaFarmaceutica} onChange={handleInputChange} />

            <div className="botones-modal">
              <button onClick={guardarMedicamento} className="btn-guardar">Guardar</button>
              <button onClick={cerrarModal} className="btn-cancelar">Cancelar</button>
            </div>
          </div>
        </div>
      )}

      {/* 🔥 Combobox y campos para recetar medicamentos existentes */}
      <div className="agregar-medicamento">
        <h3>Agregar Medicamento a la Receta</h3>
        <select value={medicamentoSeleccionado} onChange={(e) => setMedicamentoSeleccionado(e.target.value)}>
          <option value="">Selecciona un medicamento</option>
          {medicamentosDisponibles.map((med, index) => (
            <option key={index} value={med.nombre}>
              {med.nombre}
            </option>
          ))}
        </select>

        <input type="text" placeholder="Dosis" value={dosis} onChange={(e) => setDosis(e.target.value)} />
        <input type="text" placeholder="Frecuencia" value={frecuencia} onChange={(e) => setFrecuencia(e.target.value)} />
        <input type="text" placeholder="Duración" value={duracion} onChange={(e) => setDuracion(e.target.value)} />
        
        <button type="button" onClick={agregarMedicamentoRecetado}>Agregar a Receta</button>
      </div>

      {/* Lista de medicamentos recetados */}
      <div className="tabla-medicamentos">
        <h4>Medicamentos Recetados:</h4>
        <table>
          <thead>
            <tr>
              <th>Medicamento</th>
              <th>Dosis</th>
              <th>Frecuencia</th>
              <th>Duración</th>
            </tr>
          </thead>
          <tbody>
            {medicamentosRecetados.map((med, index) => (
              <tr key={index}>
                <td>{med.nombre}</td>
                <td>{med.dosis}</td>
                <td>{med.frecuencia}</td>
                <td>{med.duracion}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* 🔥 Botón para guardar toda la receta */}
      <button className="btn-guardar-receta" onClick={guardarRecetaCompleta}>
        Guardar Receta Completa
      </button>

      <div className="footer-receta">
        <p>Hospital La Paz | Emergencias 24h | Tel. +502 7485-9658</p>
      </div>
    </div>
  );
}
