<script>
  import { createEventDispatcher, onMount } from 'svelte';

  let nombreSeguro; // Valor de respaldo
  let logoUrl = '';

  const dispatch = createEventDispatcher();

  onMount(async () => {
    try {
      const res = await fetch('http://127.0.0.1:8000/estructura_web/por-titulo/Header y Footer');

      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }

      const data = await res.json();
      nombreSeguro = data.nombre_seguro;
      logoUrl = data.logo;
    } catch (error) {
      console.error("Error cargando datos de Header:", error);
      nombreSeguro = 'Nombre sin cargar';
      logoUrl = 'Sin logo';
    }
  });

  function toggleMenu() {
    dispatch('toggleMenu');
  }
</script>

<header class="header">
  <div class="logo-container">
    {#if logoUrl}
      <img src={logoUrl} alt="Logo del seguro" class="logo" />
    {/if}
    <h1 class="nombre-seguro">{nombreSeguro}</h1>
  </div>

  <div class="menu-icon" on:click={toggleMenu} role="button" tabindex="0">
    ☰
  </div>
</header>

<style>
  .header {
    background-color: #fffd59;
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

  .menu-icon {
    font-size: 25px;
    cursor: pointer;
    padding: 8px 60px;
    color: black;
    border-radius: 5px;
  }
</style>
