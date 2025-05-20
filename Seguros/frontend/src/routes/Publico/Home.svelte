<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import { API_BASE_URL } from "$lib/api";

  let data = {
    titulo: '',
    banner: {
      titulo: '',
      subtitulo: '',
      color_inicio: '#0077b6',
      color_fin: '#90e0ef',
      imagen: '',
      texto_boton: '',
      ruta_boton: ''
    },
    secciones: []
  };

  onMount(() => {
    axios.get(`${API_BASE_URL}/estructura_web/por-id/67d0ce0fa6c3ae083f71b75f`)
      .then(res => data = res.data)
      .catch(error => {
        console.error('Error al obtener la página Inicio:', error);
        data.titulo = 'Sin título';
        data.secciones = [];
      });
  });
</script>

<main class="pagina">
  {#if data.banner}
    <section class="banner-home" style="--inicio: {data.banner.color_inicio}; --fin: {data.banner.color_fin};">
      <div class="banner-contenido">
        <div class="banner-texto fade-in">
          <h2>{data.banner.titulo}</h2>
          <p>{data.banner.subtitulo}</p>
          {#if data.banner.texto_boton}
            <button on:click={() => window.location.href = data.banner.ruta_boton}>
              {data.banner.texto_boton}
            </button>
          {/if}
        </div>
        {#if data.banner.imagen}
          <div class="banner-imagen fade-in">
            <img src={data.banner.imagen} alt="Banner" />
          </div>
        {/if}
      </div>
    </section>
  {/if}

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
    padding: 0;
  }

  .banner-home {
    width: 100vw;
    padding: 2rem 1rem;
    color: white;
    background: linear-gradient(-45deg, var(--inicio), var(--fin), var(--inicio));
    background-size: 600% 600%;
    animation: moverGradiente 12s ease infinite;
    box-sizing: border-box;
    margin-top: 0rem;
  }

  .banner-contenido {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
    gap: 2rem;
  }

  .banner-texto {
    flex: 1;
    text-align: left;
  }

  .banner-texto h2 {
    font-size: 2.5rem;
    font-weight: bold;
    margin-bottom: 0.5rem;
  }

  .banner-texto p {
    font-size: 1.2rem;
    margin-bottom: 1rem;
  }

  .banner-texto button {
    background-color: white;
    color: #006400;
    border: none;
    padding: 0.6rem 1.2rem;
    font-weight: bold;
    border-radius: 8px;
    cursor: pointer;
    transition: background 0.3s ease, transform 0.3s ease;
  }

  .banner-texto button:hover {
    background-color: #f1f1f1;
    transform: translateY(-2px);
  }

  .banner-imagen img {
    max-height: 180px;
    border-radius: 10px;
    max-width: 100%;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  }

  .contenido-seccion {
    padding: 2rem 1rem;
    text-align: center;
    max-width: 1000px;
    margin: 0 auto;
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

  @keyframes moverGradiente {
    0% { background-position: 0% 50%; }
    50% { background-position: 100% 50%; }
    100% { background-position: 0% 50%; }
  }

  @keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
  }

  @keyframes slideIn {
    from { transform: translateY(40px); opacity: 0; }
    to { transform: translateY(0); opacity: 1; }
  }

  .fade-in {
    opacity: 0;
    animation: fadeIn 1s ease forwards;
  }

  @media (max-width: 768px) {
    .banner-contenido {
      flex-direction: column;
      text-align: center;
    }

    .banner-texto {
      text-align: center;
    }
  }
</style>
