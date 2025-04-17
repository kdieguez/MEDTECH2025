import React, { useState, useEffect } from 'react';
import Calendar from 'react-calendar';
import axios from 'axios';
import 'react-calendar/dist/Calendar.css';
import './css/agendarcita.css';
import { useNavigate } from 'react-router-dom';

export default function AgendarCita() {
  const navigate = useNavigate();

  const [autorizado, setAutorizado] = useState(null);
  const [fechaSeleccionada, setFechaSeleccionada] = useState(null);
  const [modoSeleccion, setModoSeleccion] = useState('doctor');

  const [pacientes, setPacientes] = useState([]);
  const [pacienteSeleccionado, setPacienteSeleccionado] = useState('');

  const [doctores, setDoctores] = useState([]);
  const [doctorSeleccionado, setDoctorSeleccionado] = useState('');

  const [servicios, setServicios] = useState([]);
  const [servicioSeleccionado, setServicioSeleccionado] = useState('');

  const [subcategorias, setSubcategorias] = useState([]);
  const [subcategoriaSeleccionada, setSubcategoriaSeleccionada] = useState('');

  const [horasDisponibles, setHorasDisponibles] = useState([]);
  const [horaSeleccionada, setHoraSeleccionada] = useState('');

  // 🔐 Verificar permisos
  useEffect(() => {
    const datosUsuario = localStorage.getItem('usuario');
    if (datosUsuario) {
      const { rol, cargo } = JSON.parse(datosUsuario);
      setAutorizado((rol === 1 || (rol === 2 && cargo === 2)));
    } else {
      setAutorizado(false);
    }
  }, []);

  // 🧾 Cargar pacientes
  useEffect(() => {
    axios.get('http://localhost:7000/pacientes')
      .then(res => setPacientes(res.data))
      .catch(err => console.error("Error al cargar pacientes", err));
  }, []);

  // 🧾 Cargar doctores y servicios
  useEffect(() => {
    axios.get('http://localhost:7000/doctores')
      .then(res => setDoctores(res.data))
      .catch(err => console.error('Error al cargar doctores', err));

    axios.get('http://localhost:7000/servicios')
      .then(res => setServicios(res.data))
      .catch(err => console.error('Error al cargar servicios', err));
  }, []);

  // 🕒 Cargar horas disponibles
  useEffect(() => {
    if (fechaSeleccionada && doctorSeleccionado) {
      const fecha = fechaSeleccionada.toISOString().split('T')[0];
      axios.get('http://localhost:7000/citas/horarios-disponibles', {
        params: {
          idDoctor: doctorSeleccionado,
          fecha: fecha
        }
      })
        .then(res => setHorasDisponibles(res.data))
        .catch(err => {
          console.error('Error al obtener horarios disponibles', err);
          setHorasDisponibles([]);
        });
    } else {
      setHorasDisponibles([]);
    }
  }, [fechaSeleccionada, doctorSeleccionado]);

  // 📂 Cargar subcategorías y doctores por servicio
  useEffect(() => {
    if (servicioSeleccionado) {
      axios.get(`http://localhost:7000/servicios/${servicioSeleccionado}/subcategorias`)
        .then(res => setSubcategorias(res.data))
        .catch(err => console.error('Error al cargar subcategorías', err));

      if (modoSeleccion === 'servicio') {
        axios.get(`http://localhost:7000/doctores/por-servicio/${servicioSeleccionado}`)
          .then(res => setDoctores(res.data))
          .catch(err => console.error('Error al cargar doctores por servicio', err));
      }
    } else {
      setSubcategorias([]);
    }
  }, [servicioSeleccionado, modoSeleccion]);

  // 🚫 Mostrar carga
  if (autorizado === null) return <p>Cargando...</p>;

  // 🚫 Acceso denegado
  if (!autorizado) {
    return (
      <div className="denegado">
        <h2>🚫 Acceso Denegado</h2>
        <p>No tienes permisos para acceder a esta sección.</p>
        <button onClick={() => navigate('/')}>Volver al inicio</button>
      </div>
    );
  }

  const agendarCita = () => {
    if (!pacienteSeleccionado || !doctorSeleccionado || !subcategoriaSeleccionada || !horaSeleccionada) {
      alert("Por favor completa todos los campos obligatorios.");
      return;
    }

    const fecha = fechaSeleccionada.toISOString().split('T')[0];
    const fechaHora = `${fecha}T${horaSeleccionada}:00`;

    axios.post('http://localhost:7000/citas', {
      idPaciente: parseInt(pacienteSeleccionado),
      idDoctor: parseInt(doctorSeleccionado),
      idSubcategoria: parseInt(subcategoriaSeleccionada),
      fechaHora
    })
      .then(() => alert('Cita agendada con éxito'))
      .catch(err => {
        console.error('Error al agendar cita', err);
        alert('Error al agendar cita');
      });
  };

  const handleDoctorChange = (e) => {
    const id = e.target.value;
    setDoctorSeleccionado(id);
    setServicioSeleccionado('');
    setSubcategoriaSeleccionada('');
    setServicios([]);
    setSubcategorias([]);

    if (modoSeleccion === 'doctor' && id) {
      axios.get(`http://localhost:7000/servicios/por-doctor/${id}`)
        .then(res => setServicios(res.data))
        .catch(err => console.error('Error al cargar servicios del doctor', err));
    }
  };

  return (
    <div className="agendar-container">
      <div className="calendar-section">
        <h2>Calendario de Citas</h2>
        <Calendar
          onClickDay={setFechaSeleccionada}
          value={fechaSeleccionada}
          minDate={new Date()}
        />
      </div>

      {fechaSeleccionada && (
        <div className="form-section">
          <h2>Agendar cita para el {fechaSeleccionada.toLocaleDateString()}</h2>

          <label>Seleccionar paciente:</label>
          <select
            value={pacienteSeleccionado}
            onChange={e => setPacienteSeleccionado(e.target.value)}
          >
            <option value="">-- Seleccionar paciente --</option>
            {pacientes.map(p => (
              <option key={p.idPaciente} value={p.idPaciente}>
                {p.nombre} {p.apellido} - DPI: {p.documentoIdentificacion}
              </option>
            ))}
          </select>

          <label>Seleccionar Doctor:</label>
          <select
            value={doctorSeleccionado}
            onChange={handleDoctorChange}
            disabled={(modoSeleccion === 'doctor' && doctores.length === 0) ||
                      (modoSeleccion === 'servicio' && !servicioSeleccionado)}
          >
            <option value="">-- Seleccionar doctor --</option>
            {doctores.map(doc => (
              <option key={doc.id} value={doc.id}>Dr. {doc.nombre} {doc.apellido}</option>
            ))}
          </select>

          <label>Seleccionar Servicio:</label>
          <select
            value={servicioSeleccionado}
            onChange={e => setServicioSeleccionado(e.target.value)}
            disabled={(modoSeleccion === 'doctor' && !doctorSeleccionado) ||
                      (modoSeleccion === 'servicio' && servicios.length === 0)}
          >
            <option value="">-- Seleccionar servicio --</option>
            {servicios.map(s => (
              <option key={s.id} value={s.id}>{s.nombre}</option>
            ))}
          </select>

          <label>Seleccionar Subcategoría:</label>
          <select
            value={subcategoriaSeleccionada}
            onChange={e => setSubcategoriaSeleccionada(e.target.value)}
            disabled={!servicioSeleccionado}
          >
            <option value="">-- Seleccionar subcategoría --</option>
            {subcategorias.map(sc => (
              <option key={sc.id} value={sc.id}>{sc.nombre}</option>
            ))}
          </select>

          <label>Seleccionar hora:</label>
          <select
            value={horaSeleccionada}
            onChange={e => setHoraSeleccionada(e.target.value)}
            disabled={!doctorSeleccionado || horasDisponibles.length === 0}
          >
            <option value="">
              {!doctorSeleccionado
                ? '-- Seleccionar hora --'
                : horasDisponibles.length === 0
                ? 'No hay horas disponibles'
                : '-- Seleccionar hora --'}
            </option>
            {horasDisponibles.map(h => (
              <option key={h} value={h}>{h}</option>
            ))}
          </select>

          <button onClick={agendarCita}>Agendar Cita</button>

        </div>
      )}
      <button onClick={() => navigate('/consultarCitas')} className="btn-ir-vercitas">
      Ver citas agendadas </button>

    </div>
  );
}
