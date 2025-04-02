import { useState, useEffect } from 'react';
import axios from 'axios';
import './css/formularioDoctor.css';
import EspecialidadesCombo from './EspecialidadesCombo';
import { jwtDecode } from "jwt-decode";
import { useNavigate } from 'react-router-dom';

const FormularioDoctor = () => {
  const navigate = useNavigate();
  const [idUsuario, setIdUsuario] = useState(null);
  const [fotografia, setFotografia] = useState('');
  const [numColegiado, setNumColegiado] = useState('');
  const [telefonos, setTelefonos] = useState(['']);
  const [especialidades, setEspecialidades] = useState([
    { idEspecialidad: '', universidad: '', fechaGraduacion: '', fotografiaTitulo: '' }
  ]);

  useEffect(() => {
    try {
      const token = localStorage.getItem("token");
      if (!token) throw new Error("Falta token");

      const decoded = jwtDecode(token);
      if (decoded.rol !== "2" || decoded.cargo !== 1) throw new Error("Acceso no autorizado");

      setIdUsuario(decoded.id);
    } catch (err) {
      alert("Acceso denegado. Inicia sesión con una cuenta válida.");
      navigate("/login");
    }
  }, []);

  if (!idUsuario) return null;

  const agregarTelefono = () => setTelefonos([...telefonos, '']);
  const eliminarTelefono = index => {
    const nuevos = [...telefonos];
    nuevos.splice(index, 1);
    setTelefonos(nuevos);
  };
  const actualizarTelefono = (index, valor) => {
    const nuevos = [...telefonos];
    nuevos[index] = valor;
    setTelefonos(nuevos);
  };

  const agregarEspecialidad = () => {
    setEspecialidades([...especialidades, {
      idEspecialidad: '', universidad: '', fechaGraduacion: '', fotografiaTitulo: ''
    }]);
  };

  const actualizarEspecialidad = (index, campo, valor) => {
    const nuevas = [...especialidades];
    nuevas[index][campo] = valor;
    setEspecialidades(nuevas);
  };

  const actualizarIdEspecialidad = (index, idSeleccionado) => {
    const nuevas = [...especialidades];
    nuevas[index].idEspecialidad = idSeleccionado;
    setEspecialidades(nuevas);
  };

  const eliminarEspecialidad = index => {
    const nuevas = [...especialidades];
    nuevas.splice(index, 1);
    setEspecialidades(nuevas);
  };

  const enviarFormulario = () => {
    const especialidadesProcesadas = especialidades.map(esp => ({
      idEspecialidad: esp.idEspecialidad,
      universidad: esp.universidad,
      fechaGraduacion: esp.fechaGraduacion,
      fotografiaTitulo: esp.fotografiaTitulo
    }));

    const data = {
      fotografia,
      numColegiado,
      idUsuario,
      telefonos,
      especialidades: especialidadesProcesadas
    };

    console.log("Datos enviados al backend:", data);

    axios.post('http://localhost:7000/doctores/perfil', data)
      .then(() => alert('Perfil guardado exitosamente'))
      .catch(err => alert('Error al guardar: ' + (err.response?.data || err.message)));
  };

  return (
    <div className="formulario-doctor">
      <h2>Completar Perfil Profesional</h2>

      <label>Fotografía (URL):</label>
      <input value={fotografia} onChange={e => setFotografia(e.target.value)} />

      <label>Número de Colegiado:</label>
      <input value={numColegiado} onChange={e => setNumColegiado(e.target.value)} />

      <h3>Teléfonos</h3>
      {telefonos.map((tel, i) => (
        <div key={i}>
          <input value={tel} onChange={e => actualizarTelefono(i, e.target.value)} />
          <button type="button" onClick={() => eliminarTelefono(i)}>Eliminar</button>
        </div>
      ))}
      <button type="button" onClick={agregarTelefono}>Agregar Teléfono</button>

      <h3>Especialidades</h3>
      {especialidades.map((esp, i) => (
        <div key={i} className="especialidad-bloque">
          <EspecialidadesCombo
            onEspecialidadSeleccionada={(id) => actualizarIdEspecialidad(i, id)}
          />
          <input
            placeholder="Universidad"
            value={esp.universidad}
            onChange={e => actualizarEspecialidad(i, 'universidad', e.target.value)}
          />
          <input
            type="date"
            value={esp.fechaGraduacion}
            onChange={e => actualizarEspecialidad(i, 'fechaGraduacion', e.target.value)}
          />
          <input
            placeholder="URL del título"
            value={esp.fotografiaTitulo}
            onChange={e => actualizarEspecialidad(i, 'fotografiaTitulo', e.target.value)}
          />
          <button type="button" onClick={() => eliminarEspecialidad(i)}>Eliminar</button>
        </div>
      ))}
      <button type="button" onClick={agregarEspecialidad}>Agregar Especialidad</button>

      <hr />
      <button type="button" onClick={enviarFormulario}>Guardar Perfil</button>
    </div>
  );
};

export default FormularioDoctor;
