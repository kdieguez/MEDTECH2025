<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import { API_BASE_URL } from "$lib/api";

  let data = {
    titulo: '',
    secciones: []
  };

  onMount(() => {
    axios.get(`${API_BASE_URL}/estructura_web/por-id/67d1359f061ce7f3df2dcc93`)
      .then(res => {
        data = res.data;
        if (!data.secciones) data.secciones = [];
      })
      .catch(error => {
        console.error('Error al obtener la página:', error);
        data.titulo = 'Sin título';
        data.secciones = [];
      });
  });
</script>

<main class="pagina">
  <h1>{data.titulo}</h1>

  {#each data.secciones as seccion}
    <section class="contenido-seccion">
      {#if seccion.tipo === 'texto'}
        <div class="texto-html">{@html seccion.valor}</div>

      {:else if seccion.tipo === 'imagen'}
        {#if seccion.valor.includes(',')}
          <div class="carrusel">
            {#each seccion.valor.split(',') as img}
              <img class="imagen-seccion" src={img.trim()} alt="Imagen carrusel" />
            {/each}
          </div>
        {:else}
          <img class="imagen-seccion" src={seccion.valor} alt="Imagen" />
        {/if}

      {:else if seccion.tipo === 'video'}
        <div class="video-container">
          <iframe src={seccion.valor} frameborder="0" allowfullscreen></iframe>
        </div>

      {:else if seccion.tipo === 'boton'}
        {#if seccion.valor.includes('|')}
          <button
            class="boton-seccion"
            on:click={() => window.location.href = seccion.valor.split('|')[1].trim()}
          >
            {seccion.valor.split('|')[0].trim()}
          </button>
        {/if}

      {:else if seccion.tipo === 'tarjeta'}
        <div class="tarjeta-animada">
          <div class="tarjeta-contenido">{@html seccion.valor}</div>
        </div>
      {/if}
    </section>
  {/each}
</main>

<style>
  .pagina {
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 2rem 1rem;
    max-width: 1000px;
    margin-inline: auto;
  }

  h1 {
    text-align: center;
    color: #1c2c40;
    margin-bottom: 2rem;
  }

  .contenido-seccion {
    margin-bottom: 2rem;
    text-align: center;
  }

  .texto-html {
    color: #2c3e50;
    font-size: 1.1rem;
    text-align: justify;
  }

  .imagen-seccion {
    max-width: 100%;
    border-radius: 10px;
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
    margin-bottom: 1rem;
  }

  .carrusel {
    display: flex;
    overflow-x: auto;
    gap: 1rem;
    padding: 1rem;
  }

  .carrusel img {
    max-height: 200px;
    border-radius: 10px;
    flex: 0 0 auto;
  }

  .video-container {
    position: relative;
    padding-bottom: 56.25%;
    height: 0;
    overflow: hidden;
    margin-top: 1rem;
  }

  .video-container iframe {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    border: none;
  }

  .boton-seccion {
    background-color: #ffd000;
    color: #1c2c40;
    border: none;
    padding: 0.8rem 1.6rem;
    border-radius: 30px;
    font-weight: bold;
    font-size: 1rem;
    cursor: pointer;
    transition: transform 0.3s ease;
  }

  .boton-seccion:hover {
    transform: scale(1.05);
  }

  .tarjeta-animada {
    background: linear-gradient(135deg, #ffd000, #fff);
    padding: 2rem;
    border-radius: 20px;
    box-shadow: 0 6px 20px rgba(0,0,0,0.1);
    animation: slideIn 0.5s ease-out;
    margin-top: 1rem;
    font-size: 1.2rem;
    color: #1c2c40;
    font-weight: bold;
    transition: transform 0.3s ease;
  }

  .tarjeta-animada:hover {
    transform: scale(1.02);
  }

  @keyframes slideIn {
    from { transform: translateY(40px); opacity: 0; }
    to { transform: translateY(0); opacity: 1; }
  }
</style>