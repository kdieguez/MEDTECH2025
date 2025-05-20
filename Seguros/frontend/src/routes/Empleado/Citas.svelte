<script>
  import VerCitas from './VerCitas.svelte';
  import CrearCita from './CrearCita.svelte';

  let modo = "ver";
</script>

<div class="citas-contenedor">
  <div class="citas-nav">
    <button class:activo={modo === 'ver'} on:click={() => modo = 'ver'}>Ver Citas</button>
    <button class:activo={modo === 'crear'} on:click={() => modo = 'crear'}>Crear Cita</button>
  </div>

  {#if modo === 'ver'}
    <VerCitas />
  {:else if modo === 'crear'}
    <CrearCita />
  {/if}
</div>


<style>
  .citas-nav {
    display: flex;
    flex-direction: row;
    justify-content: center;
    gap: 15px;
    margin-bottom: 20px;
  }

  .citas-contenedor {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 30px;
  }

  .citas-nav button {
    padding: 10px 25px;
    font-size: 15px;
    border-radius: 6px;
    border: 1px solid #003366;
    background-color: white;
    color: #003366;
    cursor: pointer;
    transition: background-color 0.3s, color 0.3s;
  }

  .citas-nav button:hover {
    background-color: #003366;
    color: white;
  }

  .citas-nav button.activo {
    background-color: #003366;
    color: white;
    font-weight: bold;
  }
</style>
