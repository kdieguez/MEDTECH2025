import React, { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom'; // 👈 importante
import './css/registro.css';

const Login = () => {
  const [formData, setFormData] = useState({
    usuario: '',
    contrasena: ''
  });

  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate(); // 👈 para redireccionar

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
        const usuario = res.data;
        setMensaje(`Bienvenido, ${usuario.usuario}`);

        // ✅ Guardar usuario en localStorage
        localStorage.setItem('usuario', JSON.stringify(usuario));

        // ✅ Redirigir al home después de 1 segundo
        setTimeout(() => {
          navigate('/home'); // o window.location.href = "/home";
        }, 1000);
      })
      .catch((err) => {
        const msg = err.response?.data?.details || "Error al iniciar sesión";
        setMensaje(String(msg));
      });
  };

  return (
    <div className="registro-container">
      <h2>Iniciar Sesión</h2>

      <form onSubmit={handleSubmit}>
        <label>Usuario</label>
        <input
          type="text"
          name="usuario"
          value={formData.usuario}
          onChange={handleChange}
          required
        />

        <label>Contraseña</label>
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
