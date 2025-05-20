<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import { API_BASE_URL } from '../../lib/api';

  let hospitales = [];
  let showModal = false;
  let editarHospital = null;
  
  let userRol = localStorage.getItem('userRol') || 'no_role';

  let nombre = "", direccion = "", telefono = "", email = "", imagen = "", servicios = "";

  onMount(() => {
    obtenerHospitales();
  });

  async function obtenerHospitales() {
    const res = await axios.get(`${API_BASE_URL}/hospitales/`);
    hospitales = res.data;
  }

  function abrirModalCrear() {
    showModal = true;
    editarHospital = null;
    nombre = direccion = telefono = email = imagen = servicios = "";
  }

  function abrirModalEditar(hospital) {
    showModal = true;
    editarHospital = hospital;
    nombre = hospital.nombre;
    direccion = hospital.direccion;
    telefono = hospital.telefono;
    email = hospital.email;
    imagen = hospital.imagen;
    servicios = hospital.servicios.join(", ");
  }

  async function guardarHospital() {
    const data = {
      nombre, direccion, telefono, email, imagen,
      servicios: servicios.split(",").map(s => s.trim())
    };

    if (editarHospital) {
      await axios.put(`${API_BASE_URL}/hospitales/${editarHospital._id}`, data);
    } else {
      await axios.post(`${API_BASE_URL}/hospitales/`, data);
    }

    showModal = false;
    obtenerHospitales();
  }

  async function eliminarHospital(id) {
    await axios.delete(`${API_BASE_URL}/hospitales/${id}`);
    obtenerHospitales();
  }
</script>

<h2 class="titulo">Hospitales Afiliados</h2>

{#if userRol === 'admin' || userRol === 'empleado'}
  <button class="btn-agregar" on:click={abrirModalCrear}>➕ Agregar</button>
{/if}

<div class="hospitales-container">
  {#each hospitales as hospital}
    <div class="hospital-card">
      <img src={hospital.imagen} alt={hospital.nombre} />
      <h3>{hospital.nombre}</h3>
      <p>{hospital.direccion}</p>
      <p>{hospital.telefono}</p>
      <p>{hospital.email}</p>
      <h4>Servicios</h4>
      <ul>
        {#each hospital.servicios as servicio}
          <li>{servicio}</li>
        {/each}
      </ul>

      {#if userRol === 'admin' || userRol === 'empleado'}
        <button on:click={() => abrirModalEditar(hospital)}>Editar</button>
        <button on:click={() => eliminarHospital(hospital._id)}>Eliminar</button>
      {/if}
    </div>
  {/each}
</div>

{#if showModal}
  <div class="modal">
    <div class="modal-content">
      <h3>{editarHospital ? "Editar Hospital" : "Nuevo Hospital"}</h3>
      <input bind:value={nombre} placeholder="Nombre" />
      <input bind:value={direccion} placeholder="Dirección" />
      <input bind:value={telefono} placeholder="Teléfono" />
      <input bind:value={email} placeholder="Email" />
      <input bind:value={imagen} placeholder="Imagen URL" />
      <textarea bind:value={servicios} placeholder="Servicios (separados por coma)"></textarea>
      <button on:click={guardarHospital}>Guardar</button>
      <button on:click={() => showModal = false}>Cancelar</button>
    </div>
  </div>
{/if}

<style>
  .titulo { 
    text-align: center; 
    margin-top: 1rem; 
    font-size: 2rem; 
    }
  .btn-agregar { 
    position: fixed; 
    top: 90px; 
    right: 40px; 
  }
  .hospitales-container { 
    display: flex; 
    flex-wrap: wrap; 
    justify-content: center; 
    gap: 30px; 
    margin: 20px; 
    }
  .hospital-card { width: 400px; box-shadow: 0 0 10px #ccc; padding: 20px; border-radius: 8px; }
  .hospital-card img { width: 100%; height: 200px; object-fit: cover; margin-bottom: 10px; }
  .modal { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; justify-content: center; align-items: center; }
  .modal-content { background: white; padding: 20px; width: 400px; border-radius: 10px; display: flex; flex-direction: column; }
</style>
