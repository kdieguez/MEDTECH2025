<script>
  import { createEventDispatcher } from "svelte";

  export let isOpen = false; 
  const dispatch = createEventDispatcher();

  function selectSection(section) {
    dispatch("navigate", section);
  }
</script>

{#if isOpen}
  <ul class="menu">
    <li on:click={() => selectSection("Inicio")}><a>Inicio</a></li>
    <li on:click={() => selectSection("Subhome 1")}><a>Subhome 1</a></li>
    <li on:click={() => selectSection("Subhome 2")}><a>Subhome 2</a></li>
    <li on:click={() => selectSection("Nuestra Historia")}><a>Nuestra Historia</a></li>
    <li on:click={() => selectSection("Preguntas frecuentes")}><a>Preguntas frecuentes</a></li>
    <li on:click={() => selectSection("Contacto")}><a>Contacto</a></li>
    <li on:click={() => selectSection("Página extra 1")}><a>Página extra 1</a></li>
    <li on:click={() => selectSection("Página extra 2")}><a>Página extra 2</a></li>
    <li on:click={() => selectSection("Editar Páginas")}><a>Editar Páginas</a></li>
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

  a {
    color: black;
    text-decoration: none;
    font-family: Arial, sans-serif;
    display: block;
    padding: 10px;
  }

  @media (max-width: 480px) {
    .menu {
      width: 180px;
    }

    .menu li {
      font-size: 12px;
    }
  }
</style>
