import { useEffect, useState } from 'react';
import axios from 'axios';
import './css/formularioServicio.css';
import { useNavigate, useParams } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

const FormularioServicio = () => {
  const navigate = useNavigate();
  const { id } = useParams();

  const [nombre, setNombre] = useState('');
  const [descripcion, setDescripcion] = useState('');
  const [subcategorias, setSubcategorias] = useState([{ nombre: '', descripcion: '', precio: '' }]);
  const [doctores, setDoctores] = useState([]);
  const [doctoresSeleccionados, setDoctoresSeleccionados] = useState([]);

  const [usuarioValido, setUsuarioValido] = useState(false);

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
        setUsuarioValido(true);
      } else {
        alert("Acceso no autorizado. Inicia sesión.");
        navigate("/login");
      }
    } catch (error) {
      alert("Error al validar permisos.");
      navigate("/login");
    }

    axios.get('http://localhost:7000/doctores')
      .then(res => setDoctores(res.data))
      .catch(err => console.error("Error al cargar doctores:", err));

    if (id) {
      axios.get(`http://localhost:7000/servicios/${id}`)
        .then(res => {
          const servicio = res.data;
          setNombre(servicio.nombre);
          setDescripcion(servicio.descripcion);
          setSubcategorias(servicio.subcategorias || []);
          setDoctoresSeleccionados(servicio.doctores?.map(doc => doc.id) || []);
        })
        .catch(err => alert("Error al cargar servicio: " + (err.response?.data || err.message)));
    }

  }, [id]);

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

  const toggleDoctor = (idDoctor) => {
    if (doctoresSeleccionados.includes(idDoctor)) {
      setDoctoresSeleccionados(doctoresSeleccionados.filter(d => d !== idDoctor));
    } else {
      setDoctoresSeleccionados([...doctoresSeleccionados, idDoctor]);
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

    const endpoint = id
      ? `http://localhost:7000/servicios/${id}`
      : 'http://localhost:7000/servicios';

    const metodo = id ? axios.put : axios.post;

    metodo(endpoint, data)
      .then(() => {
        alert(`Servicio ${id ? "actualizado" : "registrado"} con éxito`);
        navigate('/servicios');
      })
      .catch(err =>
        alert("Error al guardar servicio: " + (err.response?.data || err.message))
      );
  };

  if (!usuarioValido) return null;

  return (
    <div className="formulario-servicio">
      <h2>{id ? "Editar Servicio" : "Registrar Servicio"}</h2>

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
          <button type="button" onClick={() => eliminarSubcategoria(i)}>Eliminar</button>
        </div>
      ))}
      <button type="button" onClick={agregarSubcategoria}>Agregar Subcategoría</button>

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
              {doc.nombre} {doc.apellido}
            </label>
          </div>
        ))}
      </div>

      <button className="btn-guardar" onClick={enviar}>
        {id ? "Actualizar Servicio" : "Guardar Servicio"}
      </button>
    </div>
  );
};

export default FormularioServicio;
