import { useEffect, useState } from 'react';
import axios from 'axios';
import './css/formularioServicio.css';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const FormularioServicio = () => {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [subcategorias, setSubcategorias] = useState([{ nombre: '', descripcion: '', precio: '' }]);
  const [doctores, setDoctores] = useState([]);
  const [doctoresSeleccionados, setDoctoresSeleccionados] = useState([]);

  useEffect(() => {
    try {
      const token = localStorage.getItem("token");
      if (token) {
        const decoded = jwtDecode(token);
        const esAdmin = decoded.rol === 1;
        const esEmpleadoValido = decoded.rol === 2 && decoded.cargo !== 1 && decoded.cargo !== 2;

        if (!(esAdmin || esEmpleadoValido)) {
          alert("No tienes permiso para acceder a esta página.");
          navigate("/servicios");
          return;
        }
      } else {
        alert("Acceso no autorizado. Inicia sesión.");
        navigate("/login");
        return;
      }
    } catch (error) {
      alert("Error al validar permisos.");
      navigate("/login");
    }

    axios.get('http://localhost:7000/info-doctores')
      .then(res => setDoctores(res.data))
      .catch(err => console.error("Error al cargar doctores:", err));
  }, []);

  const agregarSubcategoria = () => {
    setSubcategorias([...subcategorias, { nombre: '', descripcion: '', precio: '' }]);
  };

  const actualizarSubcategoria = (index, campo, valor) => {
    const nuevas = [...subcategorias];
    nuevas[index][campo] = valor;
    setSubcategorias(nuevas);
  };

  const eliminarSubcategoria = (index) => {
    const nuevas = [...subcategorias];
    nuevas.splice(index, 1);
    setSubcategorias(nuevas);
  };

  const toggleDoctor = (id) => {
    if (doctoresSeleccionados.includes(id)) {
      setDoctoresSeleccionados(doctoresSeleccionados.filter(d => d !== id));
    } else {
      setDoctoresSeleccionados([...doctoresSeleccionados, id]);
    }
  };

  const enviar = () => {
    const data = {
      nombre,
      descripcion,
      subcategorias: subcategorias.map(sc => ({
        nombre: sc.nombre,
        descripcion: sc.descripcion,
        precio: parseFloat(sc.precio)
      })),
      idDoctores: doctoresSeleccionados
    };

    axios.post('http://localhost:7000/servicios', data)
      .then(() => {
        alert("Servicio registrado con éxito");
        navigate('/servicios');
      })
      .catch(err => alert("Error al registrar servicio: " + (err.response?.data || err.message)));
  };

  return (
    <div className="formulario-servicio">
      <h2>Registrar Servicio</h2>

      <label>Nombre del Servicio</label>
      <input value={nombre} onChange={e => setNombre(e.target.value)} />

      <label>Descripción</label>
      <textarea value={descripcion} onChange={e => setDescripcion(e.target.value)} />

      <h3>Subcategorías</h3>
      {subcategorias.map((sub, i) => (
        <div key={i} className="subcategoria-bloque">
          <input placeholder="Nombre" value={sub.nombre} onChange={e => actualizarSubcategoria(i, 'nombre', e.target.value)} />
          <input placeholder="Descripción" value={sub.descripcion} onChange={e => actualizarSubcategoria(i, 'descripcion', e.target.value)} />
          <input type="number" placeholder="Precio (Q)" value={sub.precio} onChange={e => actualizarSubcategoria(i, 'precio', e.target.value)} />
          <button onClick={() => eliminarSubcategoria(i)}>Eliminar</button>
        </div>
      ))}
      <button onClick={agregarSubcategoria}>Agregar Subcategoría</button>

      <h3>Doctores Disponibles</h3>
      <div className="doctor-lista">
        {doctores.map(doc => (
          <div key={doc.id}>
            <label>
              <input
                type="checkbox"
                checked={doctoresSeleccionados.includes(doc.id)}
                onChange={() => toggleDoctor(doc.id)}
              />
              {doc.usuario?.nombre} {doc.usuario?.apellido}
            </label>
          </div>
        ))}
      </div>

      <button className="btn-guardar" onClick={enviar}>Guardar Servicio</button>
    </div>
  );
};

export default FormularioServicio;
