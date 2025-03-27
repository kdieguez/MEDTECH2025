import React from 'react';
import { Link } from 'react-router-dom';
import './css/Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <Link to="/">Inicio</Link>
      <Link to="/ch">CH</Link>
      <Link to="/sh">SH</Link>
      <Link to="/nh">NH</Link>
      <Link to="/contacto">Contacto</Link>
    </nav>
  );
};

export default Navbar;
