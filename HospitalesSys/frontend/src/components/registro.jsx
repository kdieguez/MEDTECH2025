import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './css/registro.css';

const Registro = () => {
  const [formData, setFormData] = useState({
    nombre: '',
    apellido: '',
    usuario: '',
    correo: '',
    contrasena: ''
  });

  const [mensaje, setMensaje] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const usuarioPayload = {
      nombre: formData.nombre,
      apellido: formData.apellido,
      usuario: formData.usuario,
      email: formData.correo,
      password: formData.contrasena,
      idRol: 4,
      habilitado: 1
    };

    try {
      const response = await fetch('http://localhost:7000/usuarios', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuarioPayload)
    });

      if (response.ok) {
        setMensaje('Usuario registrado correctamente.');
        setFormData({
          nombre: '',
          apellido: '',
          usuario: '',
          correo: '',
          contrasena: ''
        });
      } else if (response.status === 409) {
        setMensaje('El correo o usuario ya existe.');
      } else {
        setMensaje('Error al registrar el usuario.');
      }
    } catch (error) {
      console.error('Error al conectar con el backend:', error);
      setMensaje('Error de conexión con el servidor.');
    }
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
          placeholder="Nombre completo"
          value={formData.nombre}
          onChange={handleChange}
          required
        />

        <label htmlFor="apellido">Apellido</label>
        <input
          type="text"
          id="apellido"
          name="apellido"
          placeholder="Apellido completo"
          value={formData.apellido}
          onChange={handleChange}
          required
        />

        <label htmlFor="usuario">Usuario</label>
        <input
          type="text"
          id="usuario"
          name="usuario"
          placeholder="Nombre de usuario"
          value={formData.usuario}
          onChange={handleChange}
          required
        />

        <label htmlFor="correo">Correo electrónico</label>
        <input
          type="email"
          id="correo"
          name="correo"
          placeholder="correo@dominio.com"
          value={formData.correo}
          onChange={handleChange}
          required
        />

        <label htmlFor="contrasena">Contraseña</label>
        <input
          type="password"
          id="contrasena"
          name="contrasena"
          placeholder="Contraseña segura"
          value={formData.contrasena}
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