import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './css/adminusuarios.css';

const rolMap = {
  1: 'Admin',
  2: 'Empleado',
  3: 'Paciente'
};

const AdminUsuarios = () => {
  const [usuarios, setUsuarios] = useState([]);
  const [pagina, setPagina] = useState(1);
  const [totalPaginas, setTotalPaginas] = useState(1);
  const [filtros, setFiltros] = useState({
    correo: '',
    rol: '',
    fechaInicio: '',
    fechaFin: ''
  });

  const [modalOpen, setModalOpen] = useState(false);
  const [usuarioEditar, setUsuarioEditar] = useState(null);

  const fetchUsuarios = async () => {
    try {
      const params = { pagina };
      const { correo, rol, fechaInicio, fechaFin } = filtros;
      if (correo) params.correo = correo;
      if (rol) params.rol = rol;
      if (fechaInicio) params.fechaInicio = fechaInicio;
      if (fechaFin) params.fechaFin = fechaFin;

      const response = await axios.get('http://localhost:7000/admin/usuarios/paginados', { params });
      const data = response.data;
      setUsuarios(data.usuarios);
      setTotalPaginas(data.totalPaginas);
    } catch (error) {
      console.error('Error al obtener usuarios:', error);
      setUsuarios([]);
    }
  };

  useEffect(() => {
    fetchUsuarios();
  }, [pagina]);

  const handleFiltroChange = (e) => {
    const { name, value } = e.target;
    setFiltros({ ...filtros, [name]: value });
  };

  const aplicarFiltros = () => {
    setPagina(1);
    fetchUsuarios();
  };

  const abrirModalEditar = (usuario) => {
    setUsuarioEditar({ ...usuario });
    setModalOpen(true);
  };

  const cerrarModal = () => {
    setModalOpen(false);
    setUsuarioEditar(null);
  };

  const guardarCambios = async () => {
    try {
      const datos = {
        ...usuarioEditar,
        idRol: usuarioEditar.idRol === '' ? null : usuarioEditar.idRol,
        habilitado: parseInt(usuarioEditar.habilitado)
      };
      await axios.put(`http://localhost:7000/usuarios/${usuarioEditar.id}`, datos);
      cerrarModal();
      fetchUsuarios();
    } catch (error) {
      alert('Error al guardar cambios');
      console.error(error);
    }
  };

  const desactivarUsuario = async (id) => {
    if (!window.confirm('¿Estás seguro de desactivar este usuario?')) return;
    try {
      await axios.put(`http://localhost:7000/admin/usuarios/${id}/desactivar`);
      fetchUsuarios();
    } catch (error) {
      alert('Error al desactivar usuario');
    }
  };

  const activarUsuario = async (id, rol) => {
    try {
      await axios.put(`http://localhost:7000/admin/usuarios/${id}/activar`, { idRol: rol });
      fetchUsuarios();
    } catch (error) {
      alert('Error al activar usuario');
    }
  };

  return (
    <div className="admin-usuarios">
      <h2>Administración de Usuarios</h2>

      <div className="filtros">
        <input name="correo" placeholder="Correo" onChange={handleFiltroChange} />
        <input name="rol" placeholder="ID Rol" onChange={handleFiltroChange} />
        <input type="date" name="fechaInicio" onChange={handleFiltroChange} />
        <input type="date" name="fechaFin" onChange={handleFiltroChange} />
        <button onClick={aplicarFiltros}>Aplicar filtros</button>
      </div>

      <table>
        <thead>
          <tr>
            <th>ID</th><th>Nombre</th><th>Correo</th><th>Rol</th><th>Estado</th><th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(usuarios) && usuarios.length > 0 ? (
            usuarios.map(usuario => (
              <tr key={usuario.id}>
                <td>{usuario.id}</td>
                <td>{usuario.nombre} {usuario.apellido}</td>
                <td>{usuario.email}</td>
                <td>{rolMap[usuario.idRol] ?? 'Sin rol'}</td>
                <td>{usuario.habilitado === 1 ? 'Activo' : 'Inactivo'}</td>
                <td>
                  <button className="editar" onClick={() => abrirModalEditar(usuario)}>Editar</button>
                  {usuario.habilitado === 0 ? (
                    <button className="activar" onClick={() => activarUsuario(usuario.id, 2)}>Activar</button>
                  ) : (
                    <button className="desactivar" onClick={() => desactivarUsuario(usuario.id)}>Desactivar</button>
                  )}
                </td>
              </tr>
            ))
          ) : (
            <tr><td colSpan="6">No hay usuarios disponibles</td></tr>
          )}
        </tbody>
      </table>

      <div className="paginacion">
        <button disabled={pagina === 1} onClick={() => setPagina(pagina - 1)}>Anterior</button>
        <span>Página {pagina} de {totalPaginas}</span>
        <button disabled={pagina === totalPaginas} onClick={() => setPagina(pagina + 1)}>Siguiente</button>
      </div>

      {modalOpen && usuarioEditar && (
        <div className="modal">
          <div className="modal-content">
            <h3>Editar Usuario</h3>
            <input value={usuarioEditar.nombre} onChange={e => setUsuarioEditar({ ...usuarioEditar, nombre: e.target.value })} placeholder="Nombre" />
            <input value={usuarioEditar.apellido} onChange={e => setUsuarioEditar({ ...usuarioEditar, apellido: e.target.value })} placeholder="Apellido" />
            <input value={usuarioEditar.email} onChange={e => setUsuarioEditar({ ...usuarioEditar, email: e.target.value })} placeholder="Correo" />
            <input value={usuarioEditar.usuario} onChange={e => setUsuarioEditar({ ...usuarioEditar, usuario: e.target.value })} placeholder="Nombre de usuario" />
            <input
              value={usuarioEditar.idRol ?? ''}
              onChange={e => setUsuarioEditar({
                ...usuarioEditar,
                idRol: e.target.value ? parseInt(e.target.value) : null
              })}
              placeholder="Rol (1-3)"
            />
            <select value={usuarioEditar.habilitado} onChange={e => setUsuarioEditar({ ...usuarioEditar, habilitado: e.target.value })}>
              <option value="1">Activo</option>
              <option value="0">Inactivo</option>
            </select>
            <div className="modal-buttons">
              <button onClick={guardarCambios}>Guardar</button>
              <button onClick={cerrarModal}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminUsuarios;