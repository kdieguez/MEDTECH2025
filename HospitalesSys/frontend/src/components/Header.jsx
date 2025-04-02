import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './css/Header.css';

const Header = ({ usuarioLogueado, setUsuario }) => {
  const [logo, setLogo] = useState('');
  const [titulo, setTitulo] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:7000/headerfooter')
      .then(res => {
        const logoData = res.data.find(item => item.titulo === 'Logo');
        const tituloData = res.data.find(item => item.titulo === 'Titulo');
        if (logoData) setLogo(logoData.contenido);
        if (tituloData) setTitulo(tituloData.contenido);
      })
      .catch(err => console.error('Error al cargar header:', err));
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    setUsuario(null);
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="header-left">
        {logo && <img src={logo} alt="Logo" className="logo" />}
        <h1 className="titulo">{titulo}</h1>
      </div>

      <div className="header-right">
        {usuarioLogueado ? (
          <>
            <span className="nombre-usuario">Hola, {usuarioLogueado.nombre}</span>
            <button className="logout-btn" onClick={handleLogout}>Cerrar sesión</button>
          </>
        ) : (
          <Link to="/login">
            <button className="login-btn">Login</button>
          </Link>
        )}
      </div>
    </header>
  );
};

export default Header;
