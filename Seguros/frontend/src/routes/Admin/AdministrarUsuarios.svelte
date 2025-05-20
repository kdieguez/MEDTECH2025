<script>
  import { onMount } from "svelte";
  import axios from "axios";
  import Swal from "sweetalert2";
  import { API_BASE_URL } from "$lib/api";

  let usuarios = [];
  let mostrarModal = false;
  let usuarioSeleccionado = null;

  onMount(() => {
    obtenerUsuarios();
  });

  function obtenerUsuarios() {
    axios.get(`${API_BASE_URL}/usuarios/listar`)
      .then(res => {
        usuarios = res.data.usuarios;
      })
      .catch(error => {
        console.error("Error obteniendo usuarios:", error);
        Swal.fire({
          icon: "error",
          title: "Error",
          text: "No se pudieron cargar los usuarios."
        });
      });
  }

  function abrirModal(usuario) {
    usuarioSeleccionado = { ...usuario };

    if (usuarioSeleccionado.permiso_editar_paginas === undefined) {
      usuarioSeleccionado.permiso_editar_paginas = false;
    }

    mostrarModal = true;
    document.body.style.overflow = "hidden";
  }

  function cerrarModal() {
    usuarioSeleccionado = null;
    mostrarModal = false;
    document.body.style.overflow = "auto";
  }

  function guardarCambios() {
    const { _id, nombre, apellido, correo, rol, estado, permiso_editar_paginas } = usuarioSeleccionado;

    axios.put(`${API_BASE_URL}/usuarios/editar/${_id}`, {
      nombre,
      apellido,
      correo,
      rol,
      estado,
      permiso_editar_paginas
    })
    .then(res => {
      Swal.fire({
        icon: "success",
        title: "Actualizado",
        text: "Usuario actualizado correctamente"
      });
      cerrarModal();
      obtenerUsuarios();
    })
    .catch(error => {
      console.error("Error guardando cambios:", error);

      let mensaje = "Error inesperado al guardar los cambios.";
      if (error.response) {
        mensaje = error.response.data.detail || mensaje;
      }

      Swal.fire({
        icon: "error",
        title: "Error",
        text: mensaje
      });
    });
  }
</script>

<main>
  <h1>Administrar Usuarios</h1>

  <div class="usuarios-grid">
    {#each usuarios as usuario}
      <div class="usuario-card">
        <h3>{usuario.nombre} {usuario.apellido}</h3>
        <p><strong>Correo:</strong> {usuario.correo}</p>
        <p><strong>Rol:</strong> {usuario.rol || "Sin asignar"}</p>
        <p><strong>Estado:</strong> {usuario.estado ? "Activo" : "Inactivo"}</p>

        <div class="acciones">
          <button on:click={() => abrirModal(usuario)}>Editar</button>
        </div>
      </div>
    {/each}
  </div>

  {#if mostrarModal}
    <div class="modal-fondo">
      <div class="modal-contenido">
        <h2>Editar Usuario</h2>

        <label for="nombre">Nombre:</label>
        <input id="nombre" type="text" bind:value={usuarioSeleccionado.nombre} />

        <label for="apellido">Apellido:</label>
        <input id="apellido" type="text" bind:value={usuarioSeleccionado.apellido} />

        <label for="correo">Correo:</label>
        <input id="correo" type="email" bind:value={usuarioSeleccionado.correo} />

        <label for="rol">Rol:</label>
        <select id="rol" bind:value={usuarioSeleccionado.rol}>
          <option value="admin">Administrador</option>
          <option value="empleado">Empleado</option>
          <option value="paciente">Paciente</option>
          <option value="interconexiones">Interconexiones</option>
        </select>

        {#if usuarioSeleccionado.rol === "empleado"}
          <label class="checkbox">
            <input type="checkbox" bind:checked={usuarioSeleccionado.permiso_editar_paginas} />
            ¿Puede editar páginas?
          </label>
        {/if}

        <label for="estado">Estado:</label>
        <select id="estado" bind:value={usuarioSeleccionado.estado}>
          <option value={true}>Activo</option>
          <option value={false}>Inactivo</option>
        </select>

        <div class="modal-acciones">
          <button class="guardar" on:click={guardarCambios}>Guardar</button>
          <button class="cancelar" on:click={cerrarModal}>Cancelar</button>
        </div>
      </div>
    </div>
  {/if}
</main>

<style>
  main {
    padding: 2rem;
    text-align: center;
  }

  h1 {
    color: #1f2a40;
    margin-bottom: 1rem;
  }

  .usuarios-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 1rem;
    justify-content: center;
  }

  .usuario-card {
    background: #d4defc;
    border-radius: 10px;
    padding: 1.5rem;
    width: 280px;
    text-align: left;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }

  .acciones {
    margin-top: 1rem;
    text-align: center;
  }

  .acciones button {
    background: #ffd000;
    border: none;
    padding: 0.5rem 1.2rem;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
  }

  .modal-fondo {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(5px);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }

  .modal-contenido {
    background: white;
    border-radius: 10px;
    padding: 2rem;
    width: 400px;
    max-height: 90vh;
    overflow-y: auto;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
    text-align: left;
    position: relative;
  }

  .modal-contenido h2 {
    margin-top: 0;
    color: #1f2a40;
  }

  .modal-contenido label {
    display: block;
    margin-top: 1rem;
    font-weight: bold;
  }

  .modal-contenido input[type="text"],
  .modal-contenido input[type="email"],
  .modal-contenido select {
    width: 100%;
    margin-top: 0.3rem;
    padding: 0.5rem;
    border-radius: 5px;
    border: 1px solid #ccc;
  }

  .checkbox {
    margin-top: 1rem;
    display: flex;
    align-items: center;
  }

  .checkbox input {
    margin-right: 0.5rem;
  }

  .modal-acciones {
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1.5rem;
  }

  .modal-acciones .guardar {
    background-color: #ffd000;
    padding: 0.5rem 1.5rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
  }

  .modal-acciones .cancelar {
    background-color: #ccc;
    padding: 0.5rem 1.5rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-weight: bold;
  }
</style>
