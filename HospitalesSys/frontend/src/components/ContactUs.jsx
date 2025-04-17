import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/contactus.css';

function ContactUs() {
  const [secciones, setSecciones] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axios.get('http://localhost:7000/paginas/3/secciones')
      .then(response => {
        setSecciones(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error al cargar las secciones', error);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p>Cargando contenido de Contact Us...</p>;
  }

  return (
    <div className="contactus-container">
      {secciones.length > 0 ? (
        secciones.map(seccion => (
          <div key={seccion.id} className="seccion-contactus">
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

export default ContactUs;