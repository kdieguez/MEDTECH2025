import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import './css/detalleServicio.css';

const DetalleServicio = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [servicio, setServicio] = useState(null);
  const [usuarioValido, setUsuarioValido] = useState(false);

  useEffect(() => {
    axios.get(`http://localhost:7000/servicios/${id}`)
      .then(res => setServicio(res.data))
      .catch(err => console.error("Error al cargar detalle del servicio:", err));

    try {
      const token = localStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        const esAdmin = decoded.rol === 1;
        const esEmpleadoValido = decoded.rol === 2 && decoded.cargo !== 1 && decoded.cargo !== 2;

        if (esAdmin || esEmpleadoValido) {
          setUsuarioValido(true);
        }
      }
    } catch (e) {
      console.warn("Token inválido o expirado.");
    }
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

      {usuarioValido && (
        <button className="btn-editar" onClick={() => navigate(`/servicios/editar/${servicio.id}`)}>
          Editar Servicio
        </button>
      )}
    </div>
  );
};

export default DetalleServicio;
