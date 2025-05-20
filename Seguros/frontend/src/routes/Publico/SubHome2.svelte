<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import { API_BASE_URL } from '../../lib/api';

  let data = {
    titulo: '',
    porcentaje: '',
    descripcion: '',
    servicios: [],
    secciones: []
  };

  onMount(() => {
    axios.get(`${API_BASE_URL}/estructura_web/por-id/67d1353d061ce7f3df2dcc8d`)
      .then(res => {
        data = res.data;
        if (!data.servicios) data.servicios = [];
        if (!data.secciones) data.secciones = [];
      })
      .catch(error => {
        console.error('Error al obtener la página:', error);
        data.titulo = 'Sin título';
        data.porcentaje = '';
        data.descripcion = '';
        data.servicios = [];
        data.secciones = [];
      });
  });
</script>

<main class="pagina">
  <h1>{data.titulo}</h1>

  {#if data.porcentaje}
    <h2 class="porcentaje">{data.porcentaje}% de cobertura</h2>
  {/if}

  {#if data.descripcion}
    <p class="descripcion">{data.descripcion}</p>
  {/if}

  {#if data.servicios.length > 0}
    <h3>Servicios que cubre esta póliza:</h3>
    <div class="servicios-grid">
      {#each data.servicios as servicio}
        <div class="servicio-card">
          <img src={servicio.icono_url} alt="icono" class="icono-servicio" />
          <p>{servicio.nombre}</p>
        </div>
      {/each}
    </div>
  {/if}

  {#each data.secciones as seccion}
    <section class="contenido-seccion">
      {#if seccion.tipo === 'texto'}
        <div class="texto-html">{@html seccion.valor}</div>
      {:else if seccion.tipo === 'imagen'}
        {#if seccion.valor.includes(',')}
          <div class="carrusel">
            {#each seccion.valor.split(',') as imgUrl}
              <img src={imgUrl.trim()} alt="Imagen carrusel" class="imagen-seccion" />
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
        <div class="tarjeta-animada">{seccion.valor}</div>
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
    text-align: center;
  }

  h1 {
    color: #1c2c40;
    margin-bottom: 1rem;
  }

  .porcentaje {
    font-size: 2rem;
    color: #0077b6;
    font-weight: bold;
    margin-bottom: 1rem;
  }

  .descripcion {
    font-size: 1.1rem;
    color: #2c3e50;
    margin-bottom: 2rem;
  }

  .servicios-grid {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1.5rem;
    margin-bottom: 2rem;
  }

  .servicio-card {
    width: 150px;
    background: #f9f9f9;
    border-radius: 12px;
    padding: 1rem;
    box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    text-align: center;
  }

  .icono-servicio {
    max-width: 60px;
    margin-bottom: 0.5rem;
  }

  .contenido-seccion {
    margin-bottom: 2rem;
  }

  .texto-html {
    text-align: justify;
    color: #2c3e50;
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
    background-color: #f4f4f4;
    padding: 1.5rem;
    border-radius: 15px;
    box-shadow: 0 4px 14px rgba(0,0,0,0.1);
    animation: slideIn 0.5s ease;
    font-weight: bold;
    font-size: 1.2rem;
    color: #333;
  }

  @keyframes slideIn {
    from {
      transform: translateY(40px);
      opacity: 0;
    }
    to {
      transform: translateY(0);
      opacity: 1;
    }
  }
</style>