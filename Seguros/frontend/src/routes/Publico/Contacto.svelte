<script>
  import { onMount } from 'svelte';
  import axios from 'axios';

  let titulo = '';
  let contenido = '';

  onMount(() => {
    axios.get('http://127.0.0.1:8000/estructura_web/por-id/67d1359f061ce7f3df2dcc93')
      .then(res => {
        const data = res.data;
        titulo = data.titulo || 'Sin título';
        contenido = data.contenido || 'Sin contenido';
      })
      .catch(error => {
        console.error(`Error al obtener la página:`, error);
        titulo = 'Sin título';
        contenido = 'Sin contenido';
      });
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
