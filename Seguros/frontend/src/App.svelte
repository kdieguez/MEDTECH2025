<script>
  import Header from "./components/Header.svelte";
  import Navbar from "./components/Navbar.svelte";
  import Footer from "./components/Footer.svelte";

  import Home from "./routes/Estructura/Home.svelte";
  import Subhome1 from "./routes/Estructura/Subhome1.svelte";
  import Subhome2 from "./routes/Estructura/Subhome2.svelte";
  import NuestraHistoria from "./routes/Estructura/NuestraHistoria.svelte";
  import PreguntasFrecuentes from "./routes/Estructura/PreguntasFrecuentes.svelte";
  import Contacto from "./routes/Estructura/Contacto.svelte";
  import PaginaExtra1 from "./routes/Estructura/PaginaExtra1.svelte";
  import PaginaExtra2 from "./routes/Estructura/PaginaExtra2.svelte";
  import EditarPaginas from "./routes/Modificar/EditarPaginas.svelte";

  let currentPage = "Inicio";
  let menuOpen = false;

  function changePage(page) {
    currentPage = page;
    menuOpen = false; 
    window.history.pushState({}, "", "/" + page.replace(/\s+/g, "_"));
  }

  function toggleMenu() {
    menuOpen = !menuOpen; 
  }
</script>

<div class="container">
  <Header on:toggleMenu={toggleMenu} />
  <Navbar isOpen={menuOpen} on:navigate={(e) => changePage(e.detail)} />
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
    {:else if currentPage === "Página extra 1"}
      <PaginaExtra1 />
    {:else if currentPage === "Página extra 2"}
      <PaginaExtra2 />
    {:else if currentPage === "Editar Páginas"}
      <EditarPaginas />
    {/if}
  </main>

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
</style>
