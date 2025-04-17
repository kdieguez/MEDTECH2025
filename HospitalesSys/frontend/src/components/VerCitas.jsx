import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/vercitas.css';
import { useNavigate } from 'react-router-dom';

export default function VerCitas() {
  const [citas, setCitas] = useState([]);
  const [mostrarDoctor, setMostrarDoctor] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const datosUsuario = JSON.parse(localStorage.getItem('usuario'));
    if (!datosUsuario) return;

    const { idRol, idCargo } = datosUsuario;

    if (idRol === 2 && idCargo === 1) {
      // Doctor: ver solo sus citas
      axios.get('http://localhost:7000/citas/mias', {
        headers: {
          'idUsuario': datosUsuario.id // si lo necesitas en backend
        }
      })
        .then(res => {
          setCitas(res.data);
          setMostrarDoctor(false); // no mostrar columna doctor
        })
        .catch(err => console.error('Error al cargar citas del doctor', err));
    } else {
      // Admin o Secretaria: ver todas
      axios.get('http://localhost:7000/citas')
        .then(res => {
          setCitas(res.data);
          setMostrarDoctor(true); // mostrar columna doctor
        })
        .catch(err => console.error('Error al cargar todas las citas', err));
    }
  }, []);

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
              </tr>
            );
          })}
        </tbody>
      </table>
      <button onClick={() => navigate('/agendarCitas')} className="btn-ir-vercitas">
        Agendar nueva cita
      </button>
    </div>
  );
}
