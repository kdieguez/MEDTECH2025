<script>
  import { onMount } from "svelte";
  import { navigate } from "svelte-routing";

  let isAuthenticated = false;

  function checkAuth() {
    isAuthenticated = !!localStorage.getItem("token");
  }

  function logout() {
    localStorage.removeItem("token");
    navigate("/login");
    isAuthenticated = false;
  }

  onMount(checkAuth);
</script>

<nav class="navbar">
  <ul>
    <li><a href="/">Inicio</a></li>
    {#if isAuthenticated}
      <li><a href="/clientes">Clientes</a></li>
      <li><button on:click={logout}>Cerrar Sesión</button></li>
    {:else}
      <li><a href="/login">Iniciar Sesión</a></li>
    {/if}
  </ul>
</nav>

<style>
  .navbar {
    background-color: #2c3e50;
    padding: 15px;
  }
  .navbar ul {
    display: flex;
    list-style: none;
    justify-content: space-around;
  }
  .navbar a, .navbar button {
    color: white;
    text-decoration: none;
    background: none;
    border: none;
    cursor: pointer;
  }
</style>
