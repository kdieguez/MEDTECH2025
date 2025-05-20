<script>
import axios from 'axios';
import { createEventDispatcher, onMount } from 'svelte';
import { userRol } from '../store.js';
import { API_BASE_URL } from "$lib/api";

let nombreSeguro;
let logoUrl = '';

const dispatch = createEventDispatcher();

onMount(() => {
  axios.get(`${API_BASE_URL}/estructura_web/por-id/67d655a2f9a7d53085c7359d`)
    .then(res => {
      const data = res.data;
      nombreSeguro = data.nombre_seguro;
      logoUrl = data.logo;
    })
    .catch(error => {
      console.error("Error cargando datos de Header:", error);
      nombreSeguro = 'Nombre sin cargar';
      logoUrl = 'Sin logo';
    });
});

function toggleMenu() {
  dispatch('toggleMenu');
}

function goLogin() {
  dispatch('goLogin');
}

function logout() {
  dispatch('logout');
}
</script>

<header class="header">
  <div class="logo-container">
    {#if logoUrl}
      <img src={logoUrl} alt="Logo del seguro" class="logo" />
    {/if}
    <h1 class="nombre-seguro">{nombreSeguro}</h1>
  </div>

  <div class="acciones-container">
    {#if $userRol === "no_role" || !$userRol}
      <button class="btn-login" on:click={goLogin}>Login</button>
    {:else}
      <button class="btn-logout" on:click={logout}>Logout</button>
    {/if}

    <div class="menu-icon" on:click={toggleMenu} role="button" tabindex="0">
      ☰
    </div>
  </div>
</header>

<style>
  .header {
    background-color: #ffd000;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 30px;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 80px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    z-index: 1000;
  }

  .logo-container {
    display: flex;
    align-items: center;
  }

  .logo {
    height: 60px;
    margin-right: 15px;
  }

  .nombre-seguro {
    font-size: 2rem;
    font-weight: bold;
    font-family: 'Arial', sans-serif;
  }

  .acciones-container {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .btn-login, .btn-logout {
    background-color: transparent;
    border: 2px solid black;
    border-radius: 5px;
    padding: 8px 16px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .btn-login:hover, .btn-logout:hover {
    background-color: black;
    color: white;
  }

  .menu-icon {
    font-size: 25px;
    cursor: pointer;
    padding-right: 70px;
    padding-left: 40px;
    color: black;
    border-radius: 5px;
  }

  @media (max-width: 600px) {
    .btn-login, .btn-logout {
      padding: 5px 10px;
      font-size: 12px;
    }

    .menu-icon {
      font-size: 20px;
    }
  }
</style>
