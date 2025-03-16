<script>
  import { createEventDispatcher, onMount } from 'svelte';

  let titulo = '';
  let contenido = '';

  onMount(async () => {
    try {
      const res = await fetch('http://127.0.0.1:8000/estructura_web/por-titulo/Página extra 2');

      if (!res.ok) {
        throw new Error(`Error HTTP: ${res.status}`);
      }
      const data = await res.json();

      titulo = data.titulo || 'Sin título';
      contenido = data.contenido || 'Sin contenido';

    } catch (error) {
      console.error(`Error al obtener la página:`, error);
      titulo = 'Sin título';
      contenido = 'Sin contenido';
    }
  });
</script>

<main class="pagina">
  <section>
    <h1>{titulo}</h1>
    <p>{contenido}</p>
  </section>
</main>

<style>
  .pagina {
    text-align: center;
    margin-top: 50px;
    font-family: Arial, sans-serif;
  }

  h1 {
    color: #2c3e50;
  }

  p {
    color: #7f8c8d;
  }

  .error {
    color: red;
    font-weight: bold;
  }
</style>
