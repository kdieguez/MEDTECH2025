<script>
  import axios from 'axios';
  import { onMount } from 'svelte';
  import Swal from 'sweetalert2';

  let paginas = [];
  let paginaSeleccionada = '';
  let paginaSeleccionadaId = '';

  let nombre_seguro = '';
  let logo = '';
  let footer = '';

  let bannerFrase = '';
  let bannerSubtitulo = '';
  let bannerColorInicio = '#0077b6';
  let bannerColorFin = '#90e0ef';
  let bannerImagen = '';
  let ctaTexto = '';
  let ctaRuta = '';

  let secciones = [];

  let porcentaje = '';
  let descripcion = '';
  let servicios = [];

  onMount(() => {
    obtenerPaginas();
  });

  function obtenerPaginas() {
    axios.get('http://127.0.0.1:8000/estructura_web')
      .then(res => paginas = res.data)
      .catch(() => Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudieron cargar las páginas.' }));
  }

  function cargarPaginaSeleccionada() {
    if (!paginaSeleccionadaId) return resetearCampos();

    axios.get(`http://127.0.0.1:8000/estructura_web/por-id/${paginaSeleccionadaId}`)
      .then(res => {
        const data = res.data;
        resetearCampos();
        paginaSeleccionada = data.titulo;

        if (paginaSeleccionada === "Header y Footer") {
          nombre_seguro = data.nombre_seguro || '';
          logo = data.logo || '';
          footer = data.footer || '';
        } else if (paginaSeleccionada === "Inicio") {
          const banner = data.banner || {};
          bannerFrase = banner.titulo || '';
          bannerSubtitulo = banner.subtitulo || '';
          bannerColorInicio = banner.color_inicio || '#0077b6';
          bannerColorFin = banner.color_fin || '#90e0ef';
          bannerImagen = banner.imagen || '';
          ctaTexto = banner.texto_boton || '';
          ctaRuta = banner.ruta_boton || '';
          secciones = data.secciones || [];
        } else if (paginaSeleccionada.toLowerCase().includes("subhome")) {
          porcentaje = data.porcentaje || '';
          descripcion = data.descripcion || '';
          servicios = data.servicios || [];
        } else {
          secciones = data.secciones || [];
        }
      })
      .catch(() => Swal.fire({ icon: 'error', title: 'Error', text: 'Error al cargar la página seleccionada.' }));
  }

  function guardarCambios() {
    if (!paginaSeleccionadaId) {
      Swal.fire({ icon: 'warning', title: 'Atención', text: 'Debes seleccionar una página para editar.' });
      return;
    }

    const esHeaderFooter = paginaSeleccionada === "Header y Footer";
    const esInicio = paginaSeleccionada === "Inicio";
    const esSubhome = paginaSeleccionada.toLowerCase().includes("subhome");

    let body = {};

    if (esHeaderFooter) {
      body = { nombre_seguro, logo, footer };
    } else if (esInicio) {
      if (!bannerFrase && !bannerSubtitulo && secciones.length === 0) {
        Swal.fire({ icon: 'warning', title: 'Atención', text: 'Debes completar al menos una sección o el banner.' });
        return;
      }

      body = {
        titulo: "Inicio",
        banner: {
          titulo: bannerFrase,
          subtitulo: bannerSubtitulo,
          color_inicio: bannerColorInicio,
          color_fin: bannerColorFin,
          imagen: bannerImagen,
          texto_boton: ctaTexto,
          ruta_boton: ctaRuta
        },
        secciones
      };
    } else if (esSubhome) {
      if (!porcentaje && !descripcion && servicios.length === 0) {
        Swal.fire({ icon: 'warning', title: 'Atención', text: 'Completa el porcentaje, descripción o algún servicio.' });
        return;
      }

      body = {
        porcentaje,
        descripcion,
        servicios
      };
    } else {
      if (secciones.length === 0) {
        Swal.fire({ icon: 'warning', title: 'Atención', text: 'Agrega al menos una sección.' });
        return;
      }

      body = { secciones };
    }

    const endpoint = esHeaderFooter
      ? `http://127.0.0.1:8000/estructura_web/actualizar-header-footer/${paginaSeleccionadaId}`
      : `http://127.0.0.1:8000/estructura_web/actualizar-contenido/${paginaSeleccionadaId}`;

    axios.put(endpoint, body, { headers: { 'Content-Type': 'application/json' } })
      .then(res => {
        Swal.fire({ icon: 'success', title: '¡Éxito!', text: res.data.message });
        obtenerPaginas();
      })
      .catch(error => {
        const mensaje = error.response?.data?.detail || 'Error inesperado al guardar cambios.';
        Swal.fire({ icon: 'error', title: 'Error', text: mensaje });
      });
  }

  function resetearCampos() {
    nombre_seguro = '';
    logo = '';
    footer = '';
    bannerFrase = '';
    bannerSubtitulo = '';
    bannerColorInicio = '#0077b6';
    bannerColorFin = '#90e0ef';
    bannerImagen = '';
    ctaTexto = '';
    ctaRuta = '';
    secciones = [];
    porcentaje = '';
    descripcion = '';
    servicios = [];
  }

  function agregarSeccion(tipo) {
    secciones = [...secciones, { tipo, valor: '' }];
  }

  function eliminarSeccion(index) {
    secciones = secciones.filter((_, i) => i !== index);
  }

  function agregarServicio() {
    servicios = [...servicios, { nombre: '', icono_url: '' }];
  }

  function eliminarServicio(index) {
    servicios = servicios.filter((_, i) => i !== index);
  }
</script>

<main class="editar-paginas">
  <h1>Editar Páginas Informativas</h1>

  <div class="select-wrapper">
    <label>Selecciona la página:</label>
    <select bind:value={paginaSeleccionadaId} on:change={cargarPaginaSeleccionada}>
      <option disabled selected value="">-- Elige una página --</option>
      {#each paginas as pagina}
        <option value={pagina._id}>{pagina.titulo}</option>
      {/each}
    </select>
  </div>

  {#if paginaSeleccionada}
    <div class="formulario">
      <h2>{paginaSeleccionada}</h2>

      {#if paginaSeleccionada === "Header y Footer"}
        <div class="campo"><label>Nombre del Seguro:</label><input type="text" bind:value={nombre_seguro} /></div>
        <div class="campo"><label>Logo (URL):</label><input type="text" bind:value={logo} /></div>
        <div class="campo"><label>Footer:</label><textarea rows="3" bind:value={footer}></textarea></div>

      {:else if paginaSeleccionada === "Inicio"}
        <h3>Banner</h3>
        <div class="campo"><label>Frase del Banner:</label><input type="text" bind:value={bannerFrase} /></div>
        <div class="campo"><label>Subtítulo del Banner:</label><input type="text" bind:value={bannerSubtitulo} /></div>
        <div class="campo colores">
          <div><label>Color Inicio:</label><input type="color" bind:value={bannerColorInicio} /></div>
          <div><label>Color Fin:</label><input type="color" bind:value={bannerColorFin} /></div>
        </div>
        <div class="campo"><label>Imagen del Banner (URL):</label><input type="text" bind:value={bannerImagen} /></div>
        <div class="campo"><label>Texto del Botón:</label><input type="text" bind:value={ctaTexto} /></div>
        <div class="campo"><label>Ruta del Botón:</label><input type="text" bind:value={ctaRuta} /></div>

        <h3>Secciones</h3>
        {#each secciones as seccion, index}
          <div class="seccion-item">
            <label>Tipo:</label>
            <select bind:value={seccion.tipo}>
              <option value="texto">Texto (HTML permitido)</option>
              <option value="imagen">Imagen / Carrusel</option>
              <option value="video">Video / Mapa</option>
              <option value="boton">Botón (texto|ruta)</option>
              <option value="tarjeta">Tarjeta animada</option>
            </select>
            <label>Contenido:</label>
            <textarea rows="3" bind:value={seccion.valor}></textarea>
            <button class="eliminar" on:click={() => eliminarSeccion(index)}>Eliminar</button>
          </div>
        {/each}
        <div class="botones-secciones">
          <button on:click={() => agregarSeccion('texto')}>+Texto</button>
          <button on:click={() => agregarSeccion('imagen')}>+Imagen/carrusel</button>
          <button on:click={() => agregarSeccion('video')}>+Video/Mapa</button>
          <button on:click={() => agregarSeccion('boton')}>+Botón</button>
          <button on:click={() => agregarSeccion('tarjeta')}>+Tarjeta</button>
        </div>

      {:else if paginaSeleccionada.toLowerCase().includes('subhome')}
        <h3 class="subhome-subtitulo">Subhome de Póliza</h3>
        <div class="campo"><label>Porcentaje de cobertura:</label><input type="number" bind:value={porcentaje} /></div>
        <div class="campo"><label>Descripción:</label><textarea rows="3" bind:value={descripcion}></textarea></div>

        <h4>Servicios que ofrece</h4>
        {#each servicios as servicio, index}
          <div class="seccion-item">
            <label>Nombre del servicio:</label>
            <input type="text" bind:value={servicio.nombre} />
            <label>Icono del servicio (URL):</label>
            <input type="text" bind:value={servicio.icono_url} />
            <button class="eliminar" on:click={() => eliminarServicio(index)}>Eliminar</button>
          </div>
        {/each}
        <div class="botones-secciones">
          <button on:click={agregarServicio}>+ Agregar Servicio</button>
        </div>

      {:else}
        <h3>Secciones</h3>
        {#each secciones as seccion, index}
          <div class="seccion-item">
            <label>Tipo:</label>
            <select bind:value={seccion.tipo}>
              <option value="texto">Texto (HTML permitido)</option>
              <option value="imagen">Imagen / Carrusel</option>
              <option value="video">Video / Mapa</option>
              <option value="boton">Botón (texto|ruta)</option>
              <option value="tarjeta">Tarjeta animada</option>
            </select>
            <label>Contenido:</label>
            <textarea rows="3" bind:value={seccion.valor}></textarea>
            <button class="eliminar" on:click={() => eliminarSeccion(index)}>Eliminar</button>
          </div>
        {/each}
        <div class="botones-secciones">
          <button on:click={() => agregarSeccion('texto')}>+ Texto</button>
          <button on:click={() => agregarSeccion('imagen')}>+ Imagen</button>
          <button on:click={() => agregarSeccion('video')}>+ Video</button>
          <button on:click={() => agregarSeccion('boton')}>+ Botón</button>
          <button on:click={() => agregarSeccion('tarjeta')}>+ Tarjeta</button>
        </div>
      {/if}

      <div class="botones">
        <button class="guardar" on:click={guardarCambios}>Guardar</button>
        <button class="cancelar" on:click={resetearCampos}>Cancelar</button>
      </div>
    </div>
  {/if}
</main>

<style>
  .editar-paginas {
    max-width: 850px;
    margin: auto;
    padding: 2rem;
    font-family: Arial, sans-serif;
  }

  h1, h2, h3 {
    text-align: center;
    color: #1c2c40;
  }

  .select-wrapper {
    margin-bottom: 2rem;
    text-align: center;
  }

  select {
    padding: 0.6rem;
    border-radius: 8px;
    border: 2px solid #ffd000;
    background: #fff;
    font-size: 1rem;
    width: 100%;
    max-width: 400px;
  }

  .formulario {
    background: #f9f9f9;
    border-radius: 10px;
    padding: 2rem;
    box-shadow: 0 0 12px rgba(0, 0, 0, 0.05);
  }

  .campo, .seccion-item {
    margin-bottom: 1rem;
  }

  .campo label, .seccion-item label {
    display: block;
    font-weight: bold;
    margin-bottom: 0.3rem;
    color: #333;
  }

  input[type="text"], input[type="number"], textarea, select {
    width: 100%;
    padding: 0.6rem;
    border-radius: 6px;
    border: 1px solid #ccc;
  }

  .colores {
    display: flex;
    gap: 1rem;
  }

  .botones {
    margin-top: 2rem;
    display: flex;
    justify-content: center;
    gap: 1rem;
  }

  .guardar, .cancelar, .eliminar {
    padding: 0.6rem 1.2rem;
    border: none;
    border-radius: 20px;
    font-weight: bold;
    cursor: pointer;
    font-size: 1rem;
  }

  .guardar {
    background-color: #ffd000;
    color: #1c2c40;
  }

  .cancelar {
    background-color: #ddd;
    color: #333;
  }

  .eliminar {
    background-color: #f44336;
    color: white;
    margin-top: 0.5rem;
  }

  .botones-secciones {
    display: flex;
    gap: 1rem;
    justify-content: center;
    margin-top: 1rem;
    flex-wrap: wrap;
  }

  .botones-secciones button {
    background-color: #0077b6;
    color: white;
    border: none;
    border-radius: 10px;
    padding: 0.6rem 1rem;
    font-weight: bold;
    cursor: pointer;
  }

  .botones-secciones button:hover {
    background-color: #005f91;
  }

  .subhome-subtitulo {
    text-align: center;
    margin-top: 1.5rem;
    font-size: 1.3rem;
    color: #1c2c40;
    font-weight: bold;
  }

  @media (max-width: 600px) {
    .colores {
      flex-direction: column;
    }
  }
</style>