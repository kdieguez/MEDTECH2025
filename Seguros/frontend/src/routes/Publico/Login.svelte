<script>
  import { createEventDispatcher } from 'svelte';
  import { accessToken, userRol, userCorreo, userEstado } from '../../store.js';
  import axios from 'axios';
  import { API_BASE_URL } from "$lib/api";

  const dispatch = createEventDispatcher();

  let correo = '';
  let contrasena = '';
  let mensaje = '';

  function loginHandler() {
    mensaje = '';

    if (!correo || !contrasena) {
      mensaje = 'Completa todos los campos.';
      return;
    }

    axios.post(`{API_BASE_URL}/autenticacion/login`, {
      correo,
      contrasena
    })
    .then(response => {
      const data = response.data;

      accessToken.set(data.access_token);
      userRol.set(data.rol);
      userCorreo.set(correo);
      userEstado.set(data.estado);

      localStorage.setItem('accessToken', data.access_token);
      localStorage.setItem('userRol', data.rol);
      localStorage.setItem('userCorreo', correo);
      localStorage.setItem('userEstado', data.estado);
      localStorage.setItem('correo', correo);

      verificarPerfil();
    })
    .catch(error => {
      console.error(error);

      if (error.response) {
        mensaje = error.response.data.detail || 'Error al iniciar sesión.';
      } else {
        mensaje = 'Error en la conexión con el servidor.';
      }
    });
  }

  function verificarPerfil() {
    axios.get(`{API_BASE_URL}/usuarios/perfil/${correo}`)
      .then(response => {
        const { perfilCompleto } = response.data;

        if (!perfilCompleto) {
          dispatch('goCompletarPerfil');
        } else {
          dispatch('loginSuccess');
        }
      })
      .catch(error => {
        console.error('Error al verificar el perfil:', error);
        dispatch('loginSuccess');
      });
  }

  function irARegistro() {
    dispatch('goRegistro');
  }

  function olvidoContrasena() {
    dispatch('goOlvideContrasena');
  }
</script>

<div class="login-wrapped">
  <div class="login-card">
    <h2>Inicio de sesión</h2>
    <p>Llenar todos los campos</p>
    <div class="input-group">
      <input type="email" id="correo" bind:value={correo} required />
      <label for="correo">Correo electrónico</label>
    </div>
    <div class="input-group">
      <input type="password" id="contrasena" bind:value={contrasena} required />
      <label for="contrasena">Contraseña</label>
    </div>
    <button class="btn-login" on:click={loginHandler}>Ingresar</button>
    {#if mensaje}
      <p class="mensaje-error">{mensaje}</p>
    {/if}
    <p class="link-text">
      <a href="#" on:click|preventDefault={olvidoContrasena}>¿Olvidaste tu contraseña?</a>
    </p>
    <p class="link-text">
      ¿No tienes cuenta?
      <a href="#" on:click|preventDefault={irARegistro}>Regístrate</a>
    </p> 
  </div>
</div>

<style>
  .login-card {
    background: white;
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0px 12px 24px rgba(0, 0, 0, 0.15);
    width: 100%;
    text-align: center;
    transition: 0.4s ease;
    min-width: 350px;
    margin-bottom: 50px;
    margin-top: 20px;
  }

  .login-card:hover {
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

  .input-group {
    position: relative;
    margin: 30px 0 20px;
  }

  .input-group input {
    width: 100%;
    padding: 10px 5px;
    font-size: 1rem;
    border: none;
    border-bottom: 2px solid #ccc;
    outline: none;
    transition: border-color 0.3s;
    background: transparent;
  }

  .input-group input:focus {
    border-bottom: 2px solid #d4defc;
  }

  .input-group label {
    position: absolute;
    top: 10px;
    left: 5px;
    color: #aaa;
    font-size: 1rem;
    transition: 0.3s;
    pointer-events: none;
  }

  .input-group input:focus + label,
  .input-group input:valid + label {
    top: -14px;
    font-size: 0.8rem;
    color: #000;
  }

  .btn-login {
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

  .btn-login:hover {
    background-color: #ffd000;
    transform: translateY(-2px);
  }

  .mensaje-error {
    color: red;
    margin-top: 10px;
    font-size: 0.9rem;
  }

  .link-text {
    margin-top: 25px;
    font-size: 0.9rem;
  }

  .link-text a {
    color: #007BFF;
    cursor: pointer;
    text-decoration: none;
    font-weight: bold;
  }

  .link-text a:hover {
    text-decoration: underline;
  }

  @media (max-width: 500px) {
    .login-card {
        padding: 30px 20px;
        max-width: 90%;
    }
  }
</style>