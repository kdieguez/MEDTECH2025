<script>
  import { onMount, onDestroy } from "svelte";
  import { userRol, accessToken } from './store.js';

  import Header from "./components/Header.svelte";
  import Navbar from "./components/Navbar.svelte";
  import Footer from "./components/Footer.svelte";

  import Home from "./routes/Publico/Home.svelte";
  import Subhome1 from "./routes/Publico/Subhome1.svelte";
  import Subhome2 from "./routes/Publico/Subhome2.svelte";
  import NuestraHistoria from "./routes/Publico/NuestraHistoria.svelte";
  import PreguntasFrecuentes from "./routes/Publico/PreguntasFrecuentes.svelte";
  import Contacto from "./routes/Publico/Contacto.svelte";
  import CatalogoServicios from "./routes/Publico/CatalogoServicios.svelte";
  import CatalogoHospitales from "./routes/Publico/CatalogoHospitales.svelte";
  import Ventas from './routes/Publico/Ventas.svelte';
  import Cobros from './routes/Publico/Cobros.svelte';

  import EditarPaginas from "./routes/Empleado/EditarPaginas.svelte";
  import Registro from './routes/Publico/Registro.svelte';
  import Login from './routes/Publico/Login.svelte';
  import CompletarPerfil from "./routes/Publico/CompletarPerfil.svelte";
  import EditarPerfil from './routes/Publico/EditarPerfil.svelte';
  import AdministrarUsuarios from './routes/Admin/AdministrarUsuarios.svelte';
  import ServiciosHospitalarios from './routes/Publico/ServiciosHospitalarios.svelte';

  let currentPage = "Inicio";
  let menuOpen = false;
  let mostrarMenuAdmin = false;

  onMount(() => {
    handlePopState();

    const savedToken = localStorage.getItem('accessToken');
    const savedRol = localStorage.getItem('userRol');

    if (savedToken) {
      accessToken.set(savedToken);
    }

    if (savedRol) {
      userRol.set(savedRol);
    }

    window.addEventListener("popstate", handlePopState);
  });

  onDestroy(() => {
    window.removeEventListener("popstate", handlePopState);
  });

  function handlePopState() {
    let path = window.location.pathname.slice(1);
    if (!path) {
      currentPage = "Inicio";
      return;
    }
    currentPage = path.replace(/_/g, " ");
  }

  function changePage(page) {
    currentPage = page;
    menuOpen = false;
    mostrarMenuAdmin = false;
    window.history.pushState({}, "", "/" + page.replace(/\s+/g, "_"));
  }

  function toggleMenu() {
    menuOpen = !menuOpen;
  }

  function logout() {
    accessToken.set(null);
    userRol.set("no_role");
    localStorage.removeItem('accessToken');
    localStorage.removeItem('userRol');
    changePage("Inicio");
  }

  function toggleAdminMenu() {
    mostrarMenuAdmin = !mostrarMenuAdmin;
  }

  function irAAdministrarUsuarios() {
    changePage('Administrar Usuarios');
  }

  function irAEditarPaginas() {
    changePage('Editar Páginas');
  }
</script>

<div class="container">
  <Header
    on:toggleMenu={toggleMenu}
    on:goLogin={() => changePage('Login')}
    on:logout={logout}
    userRol={$userRol}
  />

  <Navbar
    isOpen={menuOpen}
    currentPage={currentPage}
    on:navigate={(e) => changePage(e.detail)}
  />

  <main class="content">
    {#if currentPage === "Inicio"}
      <Home />
    {:else if currentPage === "Subhome 1"}
      <Subhome1 />
    {:else if currentPage === "Subhome 2"}
      <Subhome2 />
    {:else if currentPage === "Nuestra Historia"}
      <NuestraHistoria />
    {:else if currentPage === "Preguntas frecuentes"}
      <PreguntasFrecuentes />
    {:else if currentPage === "Contacto"}
      <Contacto />
    {:else if currentPage === "Catálogo Servicios"}
      <CatalogoServicios />
    {:else if currentPage === "Servicios Asegurados"}
      <CatalogoServicios />
    {:else if currentPage === "Catálogo Hospitales"}
      <CatalogoHospitales />
    {:else if currentPage === "Servicios Hospitalarios"}
      <ServiciosHospitalarios />
    {:else if currentPage === "Login"}
      <Login 
        on:goRegistro={() => changePage('Registro')}
        on:loginSuccess={() => changePage('Inicio')}
        on:goCompletarPerfil={() => changePage('Completar Perfil')} />
    {:else if currentPage === "Registro"}
      <Registro on:goLogin={() => changePage('Login')} />
    {:else if currentPage === "Editar Páginas"}
      {#if $userRol === 'admin' || $userRol === 'empleado'}
        <EditarPaginas />
      {:else}
        <p>Acceso denegado. Solo administradores o editores pueden editar páginas.</p>
      {/if}
    {:else if currentPage === "Administrar Usuarios"}
      {#if $userRol === 'admin'}
        <AdministrarUsuarios />
      {:else}
        <p>Acceso denegado. Solo administradores pueden administrar usuarios.</p>
      {/if}
    {:else if currentPage === "Completar Perfil"}
      <CompletarPerfil />
    {:else if currentPage === "Editar Perfil"}
      <EditarPerfil />
    {:else if currentPage === "Ventas"}
      {#if $userRol === 'admin'}
        <Ventas />
      {:else}
        <p>Acceso denegado. Solo los administradores pueden acceder al módulo de ventas.</p>
      {/if}
    {:else if currentPage === "Cobros"}
      {#if $userRol === 'admin' || $userRol === 'empleado'}
        <Cobros />
      {:else}
        <p>Acceso denegado. Solo administradores o empleados pueden acceder al módulo de cobros.</p>
      {/if}
    {/if}
  </main>

  {#if $userRol === 'admin' || $userRol === 'empleado'}
    <div 
      class="tuerquita-flotante" 
      on:click={toggleAdminMenu} 
      title="Configuración"
    >
      ⚙️
    </div>

    {#if mostrarMenuAdmin}
      <div class="menu-admin-flotante">
        {#if $userRol === 'admin'}
          <button on:click={irAAdministrarUsuarios}>Administrar Usuarios</button>
          <button on:click={irAEditarPaginas}>Editar Páginas</button>
        {:else if $userRol === 'empleado'}
          <button on:click={irAEditarPaginas}>Editar Páginas</button>
        {/if}
      </div>
    {/if}
  {/if}

  <Footer />
</div>

<style>
  .container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
  }

  .content {
    flex: 1;
    margin-top: 80px;
    padding: 20px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .tuerquita-flotante {
    position: fixed;
    bottom: 100px;
    right: 40px;
    font-size: 32px;
    cursor: pointer;
    color: #333;
    z-index: 9999;
    transition: transform 0.3s ease;
  }

  .tuerquita-flotante:hover {
    transform: scale(1.2);
  }

  .menu-admin-flotante {
    position: fixed;
    bottom: 140px;
    right: 40px;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    z-index: 9999;
    min-width: 200px;
  }

  .menu-admin-flotante button {
    background: none;
    border: none;
    padding: 12px 20px;
    text-align: left;
    font-size: 14px;
    color: #333;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .menu-admin-flotante button:hover {
    background-color: #f5f5f5;
  }

  @media (max-width: 600px) {
    .tuerquita-flotante {
      bottom: 80px;
      right: 20px;
      font-size: 28px;
    }

    .menu-admin-flotante {
      bottom: 120px;
      right: 20px;
      min-width: 160px;
    }
  }
</style>