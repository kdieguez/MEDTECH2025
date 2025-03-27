import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/Header.css'

const Header = () => {
  const [data, setData] = useState({});

  useEffect(() => {
    axios.get('/api/header-footer').then(res => setData(res.data));
  }, []);

  return (
    <header className="header">
      <div className="logo-titulo">
        <img src={data['Logo']} alt="Logo" style={{ height: '100px' }} />
        <h1  className="titulo">{data['Titulo']}</h1>
      </div>
    </header>
  );
};

export default Header;
