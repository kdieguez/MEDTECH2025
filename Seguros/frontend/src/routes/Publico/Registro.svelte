<script>
  import { createEventDispatcher } from 'svelte';
  import axios from 'axios';
  import Swal from 'sweetalert2';
  import { API_BASE_URL } from "$lib/api";

  const dispatch = createEventDispatcher();

  let nombre = '';
  let apellido = '';
  let correo = '';
  let contrasena = '';
  let confirmarContrasena = '';

  function registrarHandler() {
    if (!nombre || !apellido || !correo || !contrasena || !confirmarContrasena) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Completa todos los campos para continuar.'
      });
      return;
    }

    if (contrasena !== confirmarContrasena) {
      Swal.fire({
        icon: 'error',
        title: 'Contraseñas no coinciden',
        text: 'Verifica que las contraseñas sean iguales.'
      });
      return;
    }

    axios.post(`${API_BASE_URL}/autenticacion/registro`, {
      nombre,
      apellido,
      correo,
      contrasena
    })
    .then(response => {
      Swal.fire({
        icon: 'success',
        title: 'Registro exitoso',
        text: 'Tu cuenta fue creada correctamente. Revisa tu correo electrónico.'
      });
    })
    .catch(error => {
      console.error(error);
      let errorMsg = 'Error en la conexión con el servidor.';
      if (error.response) {
        errorMsg = error.response.data.detail || 'Error al registrar.';
      }
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMsg
      });
    });
  }

  function irALogin() {
    dispatch('goLogin');
  }
</script>

<div class="registro-wrapped">
    <div class="registro-card">
      <h2>Crear cuenta</h2>
      <p>Llenar todos los campos</p>

      <div class="registro-input-group">
        <input type="text" id="registro-nombre" bind:value={nombre} required />
        <label for="registro-nombre">Nombre</label>
      </div>

      <div class="registro-input-group">
        <input type="text" id="registro-apellido" bind:value={apellido} required />
        <label for="registro-apellido">Apellido</label>
      </div>

      <div class="registro-input-group">
        <input type="email" id="registro-correo" bind:value={correo} required />
        <label for="registro-correo">Correo electrónico</label>
      </div>

      <div class="registro-input-group">
        <input type="password" id="registro-contrasena" bind:value={contrasena} required />
        <label for="registro-contrasena">Contraseña</label>
      </div>

      <div class="registro-input-group">
        <input type="password" id="registro-confirmar" bind:value={confirmarContrasena} required />
        <label for="registro-confirmar">Confirmar contraseña</label>
      </div>

      <button class="btn-registro" on:click={registrarHandler}>Registrarse</button>

      <p class="registro-link-text">
        ¿Ya tienes cuenta?
        <a href="#" on:click|preventDefault={irALogin}>Inicia sesión</a>
      </p>
    </div>
</div>

<style>
  .mensaje-resultado {
    margin-top: 10px;
    font-size: 0.95rem;
    color: #333;
    background-color: #f0f8ff;
    padding: 10px;
    border-radius: 5px;
  }

  .mensaje-resultado:before {
    content: "ℹ️ ";
  }

  .registro-card {
    background: white;
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0px 12px 24px rgba(0, 0, 0, 0.15);
    width: 100%;
    min-width: 350px;
    text-align: center;
    transition: 0.4s ease;
    margin-bottom: 50px;
    margin-top: 20px;
  }

  .registro-card:hover {
    transform: translateY(-5px);
    box-shadow: 0px 20px 40px rgba(0, 0, 0, 0.2);
  }

  h2 {
    margin-bottom: 10px;
    font-size: 2rem;
    color: #333;
  }

  p {
    color: #666;
    font-size: 0.95rem;
  }

  .registro-input-group {
    position: relative;
    margin: 30px 0 20px;
  }

  .registro-input-group input {
    width: 100%;
    padding: 10px 5px;
    font-size: 1rem;
    border: none;
    border-bottom: 2px solid #ccc;
    outline: none;
    background: transparent;
    transition: border-color 0.3s;
  }

  .registro-input-group input:focus {
    border-bottom: 2px solid #d4defc;
  }

  .registro-input-group label {
    position: absolute;
    top: 10px;
    left: 5px;
    color: #aaa;
    font-size: 1rem;
    transition: 0.3s;
    pointer-events: none;
  }

  .registro-input-group input:focus + label,
  .registro-input-group input:valid + label {
    top: -14px;
    font-size: 0.8rem;
    color: #000;
  }

  .btn-registro {
    width: 100%;
    background-color: #d4defc;
    color: #333;
    padding: 12px;
    font-weight: bold;
    border: none;
    border-radius: 25px;
    cursor: pointer;
    transition: background-color 0.3s, transform 0.2s;
    box-shadow: 0px 4px 8px rgba(0,0,0,0.1);
  }

  .btn-registro:hover {
    background-color: #ffd000;
    transform: translateY(-2px);
  }

  .mensaje-error {
    color: red;
    margin-top: 10px;
    font-size: 0.9rem;
  }

  .registro-link-text {
    margin-top: 25px;
    font-size: 0.9rem;
  }

  .registro-link-text a {
    color: #007BFF;
    cursor: pointer;
    text-decoration: none;
    font-weight: bold;
  }

  .registro-link-text a:hover {
    text-decoration: underline;
  }

  @media (max-width: 500px) {
    .registro-card {
      padding: 30px 20px;
      max-width: 90%;
    }
  }
</style>
