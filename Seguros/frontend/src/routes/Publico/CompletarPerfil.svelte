<script>
  import axios from 'axios';
  import { onMount } from 'svelte';
  import { API_BASE_URL } from '../../lib/api';

  let correo = '';
  let nombre = '';
  let fechaNacimiento = '';
  let dpi = '';
  let fotoUrl = '';

  onMount(async () => {
    correo = localStorage.getItem('userCorreo');

    // Consultar el nombre
    try {
      const response = await axios.get(`${API_BASE_URL}/usuarios/perfil/${correo}`);
      nombre = response.data.usuario.nombre;
    } catch (error) {
      console.error('Error al cargar datos de perfil:', error);
      alert('No se pudo obtener la información del perfil');
    }
  });

  async function enviarFormulario() {
    if (!fechaNacimiento || !dpi || !fotoUrl) {
      alert('Por favor completa todos los campos');
      return;
    }

    const formData = new FormData();
    formData.append('correo', correo);
    formData.append('fechaNacimiento', fechaNacimiento);
    formData.append('dpi', dpi);
    formData.append('fotoUrl', fotoUrl);

    try {
      const response = await axios.post(`${API_BASE_URL}/usuarios/perfil/completar`, formData);

      alert('Perfil completado correctamente');

      // Redirigir a Inicio
      window.location.href = '/Inicio';  // Cambia según tu ruta
    } catch (error) {
      console.error('Error al guardar el perfil:', error);
      alert('Hubo un error al guardar tu perfil');
    }
  }
</script>

<div class="form-container">
  <h2>Completa tu perfil</h2>

  <div class="form-group">
    <label>Correo</label>
    <input type="email" bind:value={correo} readonly />
  </div>

  <div class="form-group">
    <label>Nombre</label>
    <input type="text" bind:value={nombre} readonly />
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
    <label>URL de la Fotografía</label>
    <input type="url" placeholder="https://..." bind:value={fotoUrl} required />
  </div>

  <button class="btn-guardar" on:click={enviarFormulario}>Guardar Perfil</button>
</div>

<style>
  .form-container {
    width: 400px;
    margin: 40px auto;
    padding: 20px;
    background: #fff;
    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    gap: 1rem;
  }

  .form-container h2 {
    text-align: center;
    color: #333;
  }

  .form-group {
    display: flex;
    flex-direction: column;
  }

  .form-group label {
    margin-bottom: 5px;
    color: #555;
  }

  .form-group input {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 8px;
  }

  .btn-guardar {
    padding: 12px;
    background-color: #d4defc;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s;
  }

  .btn-guardar:hover {
    background-color: #ffd000;
  }
</style>
