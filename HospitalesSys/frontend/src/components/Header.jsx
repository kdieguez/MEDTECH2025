import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/Header.css';

const Header = () => {
  const [data, setData] = useState({});
  const [usuario, setUsuario] = useState(null);

  useEffect(() => {
    axios.get('/api/header-footer')
      .then(res => setData(res.data));

    // Obtener usuario desde localStorage
    const user = localStorage.getItem("usuario");
    if (user) {
      setUsuario(JSON.parse(user));
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("usuario");
    window.location.href = "/"; // o redirige a login
  };

  return (
    <header className="header">
      <div className="logo-titulo">
        <img className="logo" src={data["Logo"]} alt="Logo" />
        <h1 className="titulo">{data["Titulo"]}</h1>
      </div>
        {usuario && (
          <>
            <span className="header-link">Hola, {usuario.nombre}</span>
            <button className="logout-btn" onClick={handleLogout}>Cerrar sesión</button>
          </>
        )}
    </header>
  );
};

export default Header;
