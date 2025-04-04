import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './css/detalleServicio.css';

const DetalleServicio = () => {
  const { id } = useParams();
  const [servicio, setServicio] = useState(null);

  useEffect(() => {
    axios.get(`http://localhost:7000/servicios/${id}`)
      .then(res => setServicio(res.data))
      .catch(err => console.error("Error al cargar detalle del servicio:", err));
  }, [id]);

  if (!servicio) return <p>Cargando...</p>;

  return (
    <div className="detalle-servicio">
      <h2>{servicio.nombre}</h2>
      <p>{servicio.descripcion}</p>

      <h3>Subcategorías</h3>
      <ul>
        {servicio.subcategorias.map(sub => (
          <li key={sub.id}>
            <strong>{sub.nombre}</strong> - {sub.descripcion} - Q{sub.precio.toFixed(2)}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default DetalleServicio;
