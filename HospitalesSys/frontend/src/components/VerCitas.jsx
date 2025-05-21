import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/vercitas.css';
import { useNavigate } from 'react-router-dom';

export default function VerCitas() {
  const [citas, setCitas] = useState([]);
  const [mostrarDoctor, setMostrarDoctor] = useState(true);
  const [esDoctor, setEsDoctor] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const datosUsuario = JSON.parse(localStorage.getItem('usuario'));
    if (!datosUsuario) return;
  
    const { id, rol, cargo } = datosUsuario;

    if (rol === 2 && cargo === 1) {
      axios.get(`http://localhost:7000/citas/mias?idUsuario=${id}`)
        .then(res => {
          setCitas(res.data);
          setMostrarDoctor(false);
          setEsDoctor(true);
        })
        .catch(err => console.error('Error al cargar citas del doctor', err));
    } else {
      axios.get('http://localhost:7000/citas')
        .then(res => {
          setCitas(res.data);
          setMostrarDoctor(true);
          setEsDoctor(false);
        })
        .catch(err => console.error('Error al cargar todas las citas', err));
    }
  }, []);  

  const irAFinalizarCita = (idCita) => {
    navigate(`/formularioCita/${idCita}`);
  };

  return (
    <div className="ver-citas-container">
      <h2>Citas Agendadas</h2>
      <table className="tabla-citas">
        <thead>
          <tr>
            <th>ID</th>
            <th>Paciente</th>
            {mostrarDoctor && <th>Doctor</th>}
            <th>Servicio</th>
            <th>Subcategoría</th>
            <th>Fecha</th>
            <th>Hora</th>
            {esDoctor && <th>Acciones</th>} 
          </tr>
        </thead>
        <tbody>
          {citas.map(cita => {
            const fecha = new Date(cita.fechaHora);
            return (
              <tr key={cita.idCita}>
                <td>{cita.idCita}</td>
                <td>{cita.nombrePaciente}</td>
                {mostrarDoctor && <td>{cita.nombreDoctor}</td>}
                <td>{cita.servicio}</td>
                <td>{cita.subcategoria}</td>
                <td>{fecha.toLocaleDateString()}</td>
                <td>{fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</td>
                {esDoctor && (
                  <td>
                    <button 
                      className="btn-formulario"
                      onClick={() => irAFinalizarCita(cita.idCita)}
                    >
                      Ingresar Formulario
                    </button>
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>

      {!esDoctor && (
        <button onClick={() => navigate('/agendarCita')} className="btn-ir-vercitas">
          Agendar nueva cita
        </button>
      )}
    </div>
  );
}
