import React, { useState, useEffect } from 'react';
import axios from 'axios';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import timeGridPlugin from '@fullcalendar/timegrid';
import Modal from 'react-modal';
import './css/agendarcita.css';

Modal.setAppElement('#root');

const AgendarCita = () => {
  const [pacienteId, setPacienteId] = useState('');
  const [nombrePaciente, setNombrePaciente] = useState('');
  const [doctores, setDoctores] = useState([]);
  const [doctorId, setDoctorId] = useState('');
  const [fechaSeleccionada, setFechaSeleccionada] = useState('');
  const [horariosDisponibles, setHorariosDisponibles] = useState([]);
  const [horaSeleccionada, setHoraSeleccionada] = useState('');
  const [motivo, setMotivo] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    const usuario = JSON.parse(localStorage.getItem('usuarioAutenticado'));
    if (usuario) {
      setPacienteId(usuario.id);
      setNombrePaciente(usuario.nombre);
    }

    axios.get('http://localhost:7000/api/doctores')
      .then(response => setDoctores(response.data))
      .catch(error => console.error('Error al obtener doctores', error));
  }, []);

  const handleDateClick = async (arg) => {
    if (!doctorId) {
      alert('Selecciona un doctor antes de elegir la fecha');
      return;
    }

    setFechaSeleccionada(arg.dateStr);
    setIsModalOpen(true);

    try {
      const response = await axios.get(`http://localhost:7000/api/citas/disponibilidad`, {
        params: {
          fecha: arg.dateStr,
          doctorId
        }
      });
      setHorariosDisponibles(response.data);
    } catch (error) {
      console.error('Error al consultar disponibilidad', error);
    }
  };

  const guardarCita = async () => {
    if (!doctorId || !fechaSeleccionada || !horaSeleccionada || !motivo) {
      alert('Todos los campos son obligatorios.');
      return;
    }

    // Busca el doctor seleccionado
    const doctorSeleccionado = doctores.find(doc => doc.id === parseInt(doctorId));

    if (!doctorSeleccionado) {
      alert('No se pudo encontrar el doctor seleccionado.');
      return;
    }

    const idSubcategoria = doctorSeleccionado.idSubcategoria;

    const cita = {
      pacienteId,
      doctorId,
      idSubcategoria,
      fecha: fechaSeleccionada,
      hora: horaSeleccionada,
      motivo
    };

    try {
      await axios.post('http://localhost:7000/api/citas', cita);
      alert('Cita registrada con éxito');
      setIsModalOpen(false);
      setHoraSeleccionada('');
      setMotivo('');
    } catch (error) {
      console.error(error);
      alert('Error al guardar la cita');
    }
  };

  return (
    <div className="agenda-container">
      <h2>Calendario de Citas</h2>
      
      <div className="doctor-select">
        <label>Selecciona Doctor:</label>
        <select value={doctorId} onChange={(e) => setDoctorId(e.target.value)} required>
          <option value="">Seleccione...</option>
          {doctores.map(doc => (
            <option key={doc.id} value={doc.id}>{doc.nombre} ({doc.especialidad})</option>
          ))}
        </select>
      </div>

      <FullCalendar
        plugins={[dayGridPlugin, interactionPlugin, timeGridPlugin]}
        initialView="dayGridMonth"
        dateClick={handleDateClick}
        locale="es"
      />

      <Modal
        isOpen={isModalOpen}
        onRequestClose={() => setIsModalOpen(false)}
        className="modal-cita"
        overlayClassName="overlay-modal"
      >
        <h3>Agendar Cita para el {fechaSeleccionada}</h3>

        <div>
          <label>Paciente:</label>
          <input type="text" value={nombrePaciente} readOnly />
        </div>

        <div>
          <label>Horario Disponible:</label>
          <select value={horaSeleccionada} onChange={(e) => setHoraSeleccionada(e.target.value)} required>
            <option value="">Seleccione un horario</option>
            {horariosDisponibles.length === 0 && (
              <option disabled>No hay horarios disponibles</option>
            )}
            {horariosDisponibles.map((hora, index) => (
              <option key={index} value={hora}>{hora}</option>
            ))}
          </select>
        </div>

        <div>
          <label>Motivo de la Cita:</label>
          <textarea value={motivo} onChange={(e) => setMotivo(e.target.value)} required />
        </div>

        <button onClick={guardarCita}>Confirmar Cita</button>
        <button onClick={() => setIsModalOpen(false)}>Cancelar</button>
      </Modal>
    </div>
  );
};

export default AgendarCita;