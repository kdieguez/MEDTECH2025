<script>
  import axios from 'axios';
  import { onMount } from 'svelte';
  import { API_BASE_URL } from '../../lib/api';

  let correo = '';
  let nombre = '';
  let apellido = '';
  let fechaNacimiento = '';
  let dpi = '';
  let fotoUrl = '';
  let numAfiliacion = '';
  let numCarnet = '';

  let cargando = false;
  let mensaje = '';

  onMount(async () => {
    correo = localStorage.getItem('userCorreo');

    try {
      cargando = true;
      const response = await axios.get(`${API_BASE_URL}/usuarios/perfil/${correo}`);
      const userData = response.data.usuario;

      nombre = userData.nombre;
      apellido = userData.apellido;
      fechaNacimiento = userData.fecha_nacimiento || '';
      dpi = userData.dpi || '';
      fotoUrl = userData.fotografia || '';
      numAfiliacion = userData.num_afiliacion || '';
      numCarnet = userData.num_carnet || '';

    } catch (error) {
      console.error('Error al cargar el perfil:', error);
      mensaje = 'No se pudo cargar el perfil';
    } finally {
      cargando = false;
    }
  });

  async function actualizarPerfil() {
    mensaje = '';

    if (!nombre || !apellido || !fechaNacimiento || !dpi || !fotoUrl) {
      mensaje = 'Completa todos los campos';
      return;
    }

    const formData = new FormData();
    formData.append('correo', correo);
    formData.append('nombre', nombre);
    formData.append('apellido', apellido);
    formData.append('fechaNacimiento', fechaNacimiento);
    formData.append('dpi', dpi);
    formData.append('fotoUrl', fotoUrl);

    try {
      cargando = true;

      const response = await axios.post(`${API_BASE_URL}/usuarios/perfil/editar`, formData);
      mensaje = 'Perfil actualizado correctamente';

    } catch (error) {
      console.error('Error al actualizar el perfil:', error);
      mensaje = 'Hubo un error al guardar los cambios';
    } finally {
      cargando = false;
    }
  }
</script>

<div class="perfil-container">

  <h2>Editar Perfil</h2>

  <div class="foto-preview">
    {#if fotoUrl}
      <img src={fotoUrl} alt="Foto de Perfil" />
    {:else}
      <div class="foto-placeholder">Sin Foto</div>
    {/if}
  </div>

  <div class="form-group">
    <label>URL de la Fotografía</label>
    <input type="url" placeholder="https://..." bind:value={fotoUrl} required />
  </div>

  <div class="campos-container">

    <div class="form-group">
      <label>Correo</label>
      <input type="email" bind:value={correo} readonly />
    </div>

    <div class="form-group">
      <label>Nombre</label>
      <input type="text" bind:value={nombre} required />
    </div>

    <div class="form-group">
      <label>Apellido</label>
      <input type="text" bind:value={apellido} required />
    </div>

    <div class="form-group">
      <label>Fecha de Nacimiento</label>
      <input type="date" bind:value={fechaNacimiento} required />
    </div>

    <div class="form-group">
      <label>DPI</label>
      <input type="text" bind:value={dpi} required />
    </div>

    <div class="form-group">
      <label>Número de Afiliación</label>
      <input type="text" bind:value={numAfiliacion} readonly />
    </div>

    <div class="form-group">
      <label>Número de Carnet</label>
      <input type="text" bind:value={numCarnet} readonly />
    </div>

  </div>

  {#if mensaje}
    <p class="mensaje">{mensaje}</p>
  {/if}

  <button class="btn-guardar" on:click={actualizarPerfil} disabled={cargando}>
    {cargando ? 'Guardando...' : 'Guardar Cambios'}
  </button>

</div>

<style>
  .perfil-container {
    max-width: 700px;
    margin: 40px auto;
    padding: 30px 40px;
    background: #ffffff;
    border-radius: 15px;
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
    text-align: center;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }

  h2 {
    color: #333333;
    margin-bottom: 20px;
    font-size: 28px;
  }

  .foto-preview {
    margin-bottom: 20px;
    display: flex;
    justify-content: center;
  }

  .foto-preview img {
    width: 180px;
    height: 180px;
    object-fit: cover;
    border-radius: 50%;
    border: 5px solid #ffd000;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  }

  .foto-placeholder {
    width: 180px;
    height: 180px;
    border-radius: 50%;
    background-color: #f0f0f0;
    border: 5px dashed #ccc;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #999;
    font-size: 14px;
  }

  .campos-container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 20px;
    margin-bottom: 20px;
  }

  .form-group {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    width: 48%;
  }

  .form-group label {
    margin-bottom: 6px;
    font-weight: bold;
    color: #555;
    font-size: 14px;
  }

  .form-group input {
    width: 100%;
    padding: 12px;
    border-radius: 8px;
    border: 1px solid #ccc;
    font-size: 14px;
    box-sizing: border-box;
    transition: border 0.3s;
  }

  .form-group input:read-only {
    background-color: #f5f5f5;
    color: #888;
  }

  .form-group input:focus {
    border-color: #ffd000;
    outline: none;
  }

  .btn-guardar {
    width: 100%;
    padding: 15px;
    background-color: #ffd000;
    color: #333;
    border: none;
    border-radius: 30px;
    font-weight: bold;
    font-size: 16px;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .btn-guardar:hover {
    background-color: #ffc107;
    transform: translateY(-2px);
  }

  .btn-guardar:disabled {
    background-color: #ccc;
    cursor: not-allowed;
  }

  .mensaje {
    margin: 15px 0;
    color: #007b00;
    font-weight: 600;
  }

  @media (max-width: 768px) {
    .perfil-container {
      padding: 20px;
    }

    .campos-container {
      flex-direction: column;
    }

    .form-group {
      width: 100%;
    }
  }
</style>