import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import './css/recetamedica.css'; // Estilos de la receta
import './css/modalmedicamento.css'; // Estilos del modal

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

  useEffect(() => {
    axios.get(`http://localhost:7000/receta/${id}`)
      .then(response => setReceta(response.data))
      .catch(err => {
        console.error('Error al cargar receta', err);
        alert('No se pudo cargar la receta');
      });
  }, [id]);

  const abrirModal = () => {
    setShowModal(true);
  };

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
      })
      .catch(err => {
        console.error('Error al guardar medicamento', err);
        alert('Hubo un error al agregar el medicamento');
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

      {/* 🔵 Botón para agregar medicamento */}
      <button className="btn-agregar-medicamento" onClick={abrirModal}>
        Agregar nuevo medicamento
      </button>

      {/* Modal */}
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h2>Nuevo Medicamento</h2>

            <input
              type="text"
              name="nombre"
              placeholder="Nombre"
              value={nuevoMedicamento.nombre}
              onChange={handleInputChange}
            />
            <input
              type="text"
              name="principioActivo"
              placeholder="Principio Activo"
              value={nuevoMedicamento.principioActivo}
              onChange={handleInputChange}
            />
            <input
              type="text"
              name="concentracion"
              placeholder="Concentración"
              value={nuevoMedicamento.concentracion}
              onChange={handleInputChange}
            />
            <input
              type="text"
              name="presentacion"
              placeholder="Presentación"
              value={nuevoMedicamento.presentacion}
              onChange={handleInputChange}
            />
            <input
              type="text"
              name="formaFarmaceutica"
              placeholder="Forma Farmacéutica"
              value={nuevoMedicamento.formaFarmaceutica}
              onChange={handleInputChange}
            />

            <div className="botones-modal">
              <button onClick={guardarMedicamento} className="btn-guardar">Guardar</button>
              <button onClick={cerrarModal} className="btn-cancelar">Cancelar</button>
            </div>
          </div>
        </div>
      )}

      <div className="footer-receta">
        <p>Hospital La Paz | Emergencias 24h | Tel. +502 7485-9658</p>
      </div>
    </div>
  );
}
