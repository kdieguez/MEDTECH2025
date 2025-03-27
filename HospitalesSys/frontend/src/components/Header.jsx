import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Header = () => {
  const [data, setData] = useState({});

  useEffect(() => {
    axios.get('/api/header-footer').then(res => setData(res.data));
  }, []);

  return (
    <header className="header">
      <img src={data['Logo']} alt="Logo" style={{ height: '60px' }} />
      <h1>{data['Titulo']}</h1>
    </header>
  );
};

export default Header;
