import { useEffect, useState } from 'react';
import axios from 'axios';
import './css/catalogoServicios.css';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const CatalogoServicios = () => {
  const [servicios, setServicios] = useState([]);
  const [usuarioValido, setUsuarioValido] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:7000/servicios')
      .then(res => setServicios(res.data))
      .catch(err => console.error("Error al cargar servicios:", err));

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
    } catch (error) {
      console.warn("Token inválido o expirado.");
    }
  }, []);

  const irADetalle = (id) => {
    navigate(`/servicio/${id}`);
  };

  const irANuevo = () => {
    navigate("/formularioServicios");
  };

  return (
    <div className="catalogo-servicios">
      <h2>Catálogo de Servicios Hospitalarios</h2>

      {usuarioValido && (
        <button className="btn-agregar" onClick={irANuevo}>Agregar Nuevo Servicio</button>
      )}

      <div className="servicios-lista">
        {servicios.map(servicio => (
          <div key={servicio.id} className="servicio-card">
            <h3>{servicio.nombre}</h3>
            <p>{servicio.descripcion}</p>
            <button onClick={() => irADetalle(servicio.id)}>Ver Detalle</button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CatalogoServicios;
