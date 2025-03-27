import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './css/registro.css';

const Registro = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    usuario: '',
    correo: '',
    contrasena: '',
    contrasenaConfirmada: ''
  });

  const [mensaje, setMensaje] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (formData.contrasena !== formData.contrasenaConfirmada) {
      setMensaje("Las contraseñas no coinciden.");
      return;
    }

    const usuarioPayload = {
      nombre: formData.nombre,
      apellido: formData.apellido,
      usuario: formData.usuario,
      email: formData.correo,
      password: formData.contrasena
    };

    axios.post('http://localhost:7000/usuarios', usuarioPayload)
      .then(() => {
        setMensaje('Usuario registrado correctamente.');
        setFormData({
          nombre: '',
          apellido: '',
          usuario: '',
          correo: '',
          contrasena: '',
          contrasenaConfirmada: ''
        });
      })
      .catch((error) => {
        if (error.response && error.response.status === 409) {
          setMensaje('El correo o usuario ya existe.');
        } else {
          setMensaje('Error al registrar el usuario.');
        }
      });
  };

  return (
    <div className="registro-container">
      <h2>Registro de Paciente</h2>

      <form onSubmit={handleSubmit}>
        <label htmlFor="nombre">Nombre</label>
        <input
          type="text"
          id="nombre"
          name="nombre"
          value={formData.nombre}
          onChange={handleChange}
          required
        />

        <label htmlFor="apellido">Apellido</label>
        <input
          type="text"
          id="apellido"
          name="apellido"
          value={formData.apellido}
          onChange={handleChange}
          required
        />

        <label htmlFor="usuario">Usuario</label>
        <input
          type="text"
          id="usuario"
          name="usuario"
          value={formData.usuario}
          onChange={handleChange}
          required
        />

        <label htmlFor="correo">Correo electrónico</label>
        <input
          type="email"
          id="correo"
          name="correo"
          value={formData.correo}
          onChange={handleChange}
          required
        />

        <label htmlFor="contrasena">Contraseña</label>
        <input
          type="password"
          id="contrasena"
          name="contrasena"
          value={formData.contrasena}
          onChange={handleChange}
          required
        />

        <label htmlFor="contrasenaConfirmada">Confirmar contraseña</label>
        <input
          type="password"
          id="contrasenaConfirmada"
          name="contrasenaConfirmada"
          value={formData.contrasenaConfirmada}
          onChange={handleChange}
          required
        />

        <button type="submit">Registrarse</button>
      </form>

      {mensaje && <p>{mensaje}</p>}

      <Link to="/login">¿Ya tienes cuenta? Inicia sesión</Link>
    </div>
  );
};

export default Registro;
