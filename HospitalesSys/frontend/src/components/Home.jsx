import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/home.css';

function Home() {
  const [secciones, setSecciones] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:7000/paginas/1/secciones')
      .then(response => {
        setSecciones(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error al cargar las secciones', error);
        setLoading(false);
      });
  }, []);

  const seccionesUnicas = secciones.reduce((acc, actual) => {
    const yaExiste = acc.find(sec =>
      sec.titulo === actual.titulo &&
      sec.contenido === actual.contenido &&
      sec.imagenUrl === actual.imagenUrl &&
      sec.orden === actual.orden
    );
    if (!yaExiste) acc.push(actual);
    return acc;
  }, []);

  if (loading) {
    return <p>Cargando contenido...</p>;
  }

  return (
    <div className="home-container">
      {seccionesUnicas.length > 0 ? (
        seccionesUnicas.map(seccion => (
          <div key={seccion.id} className="seccion-home">
            <h2>{seccion.titulo}</h2>
            <div dangerouslySetInnerHTML={{ __html: seccion.contenido }} />
            {seccion.imagenUrl && <img src={seccion.imagenUrl} alt={seccion.titulo} />}
          </div>
        ))
      ) : (
        <p>No hay contenido disponible en este momento.</p>
      )}
    </div>
  );
}

export default Home;