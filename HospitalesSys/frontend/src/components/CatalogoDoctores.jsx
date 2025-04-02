import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './css/catalogoDoctores.css';

const CatalogoDoctores = () => {
  const [doctores, setDoctores] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch('http://localhost:7000/doctores')
      .then(res => res.json())
      .then(data => setDoctores(data))
      .catch(err => console.error('Error al obtener doctores:', err));
  }, []);

  return (
    <div className="catalogo-container">
      <h2>Catálogo de Doctores</h2>
      <div className="doctor-grid">
        {doctores.map((doc) => (
          <div className="doctor-card" key={doc.id}>
            <img src={doc.fotografia} alt="Foto Doctor" className="doctor-foto" />
            <h3>{doc.nombre} {doc.apellido}</h3>
            <button onClick={() => navigate(`/doctor/${doc.id}`)}>Ver más</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CatalogoDoctores;
