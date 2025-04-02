import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import './css/detalleDoctor.css';

const DetalleDoctor = () => {
  const { id } = useParams();
  const [doctor, setDoctor] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [imagenSeleccionada, setImagenSeleccionada] = useState(null);

  useEffect(() => {
    fetch(`http://localhost:7000/doctores/${id}`)
      .then(res => res.json())
      .then(data => setDoctor(data))
      .catch(err => console.error("Error al obtener detalle del doctor:", err));
  }, [id]);

  const abrirModal = (url) => {
    setImagenSeleccionada(url);
    setModalVisible(true);
  };

  const cerrarModal = () => {
    setModalVisible(false);
    setImagenSeleccionada(null);
  };

  if (!doctor) return <p>Cargando detalle del doctor...</p>;

  return (
    <div className="detalle-doctor-container">
      <h2>Detalle del Doctor</h2>
      <img src={doctor.fotografia} alt="Foto Doctor" className="detalle-foto" />
      <h3>{doctor.nombre} {doctor.apellido}</h3>
      <p><strong>Email:</strong> {doctor.email}</p>
      <p><strong>Número de colegiado:</strong> {doctor.colegiado}</p>

      <h4>Especialidades</h4>
      <ul>
        {(doctor.especialidades || []).map((e, idx) => (
          <li key={idx}>
            <p><strong>Especialidad:</strong> {e.especialidad?.nombre}</p>
            <p><strong>Universidad:</strong> {e.universidad}</p>
            <p><strong>Fecha de graduación:</strong> {e.fechaGraduacion}</p>
            <img
              src={e.fotografiaTitulo}
              alt="Título"
              className="titulo-mini"
              onClick={() => abrirModal(e.fotografiaTitulo)}
            />
          </li>
        ))}
      </ul>

      <h4>Teléfonos</h4>
      <ul>
        {(doctor.telefonos || []).map((t, idx) => (
          <li key={idx}>{t.telefono}</li>
        ))}
      </ul>

      <h4>Pacientes Atendidos</h4>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
          </tr>
        </thead>
        <tbody>
          {(doctor.pacientes || []).map((p, idx) => (
            <tr key={idx}>
              <td>{p.id}</td>
              <td>{p.nombre}</td>
              <td>{p.apellido}</td>
            </tr>
          ))}
        </tbody>
      </table>


      {modalVisible && (
        <div className="modal-overlay" onClick={cerrarModal}>
          <div className="modal-content" onClick={e => e.stopPropagation()}>
            <img src={imagenSeleccionada} alt="Imagen Ampliada" />
            <button className="cerrar-modal" onClick={cerrarModal}>Cerrar</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DetalleDoctor;
