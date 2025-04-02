import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate, Link } from 'react-router-dom';
import './css/registro.css';

const Login = ({ setUsuario }) => {
  const [identificador, setIdentificador] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    const datosLogin = { identificador, contrasena };
    console.log("Datos enviados al backend:", datosLogin);

    try {
      const res = await axios.post('http://localhost:7000/login', datosLogin);

      const { token, mostrarFormularioDoctor } = res.data;

      localStorage.setItem("token", token);
      const payload = JSON.parse(atob(token.split('.')[1]));
      localStorage.setItem("usuario", JSON.stringify(payload));
      setUsuario(payload);

      if (mostrarFormularioDoctor) {
        navigate('/formularioDoctor');
      } else {
        navigate('/');
      }

    } catch (err) {
      console.error("Error al iniciar sesión:", err);

      if (err.response && err.response.status === 403) {
        setMensaje(err.response.data);
      } else {
        setMensaje("Correo o contraseña incorrectos");
      }
    }
  };

  return (
    <div className="registro-container">
      <h2>Login</h2>

      <form onSubmit={handleLogin}>
        <label htmlFor="identificador">Correo o usuario</label>
        <input
          type="text"
          id="identificador"
          value={identificador}
          onChange={(e) => setIdentificador(e.target.value)}
          required
        />

        <label htmlFor="contrasena">Contraseña</label>
        <input
          type="password"
          id="contrasena"
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          required
        />

        <button type="submit">Iniciar sesión</button>
      </form>

      {mensaje && <p className="error">{mensaje}</p>}

      <Link to="/registro">¿No tienes cuenta? Regístrate aquí</Link>
    </div>
  );
};

export default Login;
