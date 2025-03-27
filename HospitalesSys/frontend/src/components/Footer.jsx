import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Footer = () => {
  const [data, setData] = useState({});

  useEffect(() => {
    axios.get('/api/header-footer').then(res => setData(res.data));
  }, []);

  return (
    <footer className="footer">
      <p><strong>Teléfono ambulancia:</strong> {data['Número de ambulancia principal']}</p>
      <p><strong>Correo:</strong> {data['Correo electrónico']}</p>
      <p><strong>Horario:</strong> {data['Horario']}</p>
    </footer>
  );
};

export default Footer;
