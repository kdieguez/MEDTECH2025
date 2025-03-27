import React, { useState } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import './css/registro.css'; // reutilizamos estilos

const Login = () => {
  const [formData, setFormData] = useState({
    usuario: '',
    contrasena: ''
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

    axios.post('http://localhost:7000/login', formData)
      .then((res) => {
        setMensaje(`Bienvenido, ${res.data.usuario}`);
        // Aquí podrías guardar token, redirigir, etc.
      })
      .catch((error) => {
        if (error.response) {
          setMensaje(error.response.data);
        } else {
          setMensaje("Error de conexión.");
        }
      });
  };

  return (
    <div className="registro-container">
      <h2>Iniciar Sesión</h2>

      <form onSubmit={handleSubmit}>
        <label htmlFor="usuario">Usuario</label>
        <input
          type="text"
          name="usuario"
          value={formData.usuario}
          onChange={handleChange}
          required
        />

        <label htmlFor="contrasena">Contraseña</label>
        <input
          type="password"
          name="contrasena"
          value={formData.contrasena}
          onChange={handleChange}
          required
        />

        <button type="submit">Ingresar</button>
      </form>

      {mensaje && <p>{mensaje}</p>}

      <Link to="/registro">¿No tienes cuenta? Regístrate</Link>
    </div>
  );
};

export default Login;
