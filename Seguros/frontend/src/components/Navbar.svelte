<script>
  import { createEventDispatcher } from "svelte";
  import { userRol } from '../store.js';


  export let isOpen = false;
  export let currentPage = "";

  const dispatch = createEventDispatcher();

  function selectSection(section) {
    dispatch("navigate", section);
  }
</script>

{#if isOpen}
  <ul class="menu">
    <li class:active={currentPage === "Inicio"}>
      <a
        href="/Inicio"
        on:click|preventDefault={() => selectSection("Inicio")}
      >
        Inicio
      </a>
    </li>

    <li class:active={currentPage === "Subhome 1"}>
      <a
        href="/Subhome_1"
        on:click|preventDefault={() => selectSection("Subhome 1")}
      >
        Subhome 1
      </a>
    </li>

    <li class:active={currentPage === "Subhome 2"}>
      <a
        href="/Subhome_2"
        on:click|preventDefault={() => selectSection("Subhome 2")}
      >
        Subhome 2
      </a>
    </li>

    <li class:active={currentPage === "Nuestra Historia"}>
      <a
        href="/Nuestra_Historia"
        on:click|preventDefault={() => selectSection("Nuestra Historia")}
      >
        Nuestra Historia
      </a>
    </li>

    <li class:active={currentPage === "Preguntas frecuentes"}>
      <a
        href="/Preguntas_frecuentes"
        on:click|preventDefault={() => selectSection("Preguntas frecuentes")}
      >
        Preguntas frecuentes
      </a>
    </li>

    <li class:active={currentPage === "Contacto"}>
      <a
        href="/Contacto"
        on:click|preventDefault={() => selectSection("Contacto")}
      >
        Contacto
      </a>
    </li>

    <li class:active={currentPage === "Catálogo Servicios"}>
      <a
        href="/Catalogo_Servicios"
        on:click|preventDefault={() => selectSection("Catálogo Servicios")}
      >
       Catálogo Servicios
      </a>
    </li>

    <li class:active={currentPage === "Catálogo Hospitales"}>
      <a
        href="/Catalogo_Hospitales"
        on:click|preventDefault={() => selectSection("Catálogo Hospitales")}
      >
        Catálogo Hospitales
      </a>
    </li>

     <li class:active={currentPage === "Cobros"}>
      <a
        href="/Cobros"
        on:click|preventDefault={() => selectSection("Cobros")}
      >
        Cobros
      </a>
    </li>

    {#if $userRol === 'admin' || $userRol === 'empleado'}
  <li class:active={currentPage === "Citas"}>
    <a
      href="/Citas"
      on:click|preventDefault={() => selectSection("Citas")}
    >
      Citas
    </a>
  </li>
{/if}

  </ul>
{/if}

<style>
  .menu {
    position: fixed;
    top: 90px;
    right: 20px;
    width: 220px;
    background: #d4defc;
    padding: 5px ;
    display: flex;
    flex-direction: column;
    align-items: center;
    border-radius: 5px;
    box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease-in-out;
    z-index: 10000;
  }

  .menu li {
    list-style: none;
    padding: 10px;
    font-size: 14px;
    width: 95%;
    text-align: center;
    transition: background 0.5s ease;
    cursor: pointer;
  }

  .menu li:hover {
    background-color: #a0b9ff;
    color: white;
    font-size: 16px;
  }

  .active {
    background-color: #a0b9ff;
    color: white;
    font-size: 16px;
  }

  a {
    color: black;
    text-decoration: none;
    font-family: Arial, sans-serif;
    display: block;
    padding: 10px;
  }
</style>
