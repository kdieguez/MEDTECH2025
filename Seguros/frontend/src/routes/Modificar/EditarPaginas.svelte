<script>
  import { onMount } from 'svelte';

  let paginas = [];
  let paginaSeleccionada = '';
  let paginaSeleccionadaId = '';
  let contenido = '';
  let nombre_seguro = '';
  let logo = '';
  let footer = '';
  let mensaje = '';

  onMount(async () => {
    await obtenerPaginas();
  });

  async function obtenerPaginas() {
    try {
      const res = await fetch('http://127.0.0.1:8000/estructura_web');
      paginas = await res.json();
    } catch (error) {
      console.error('Error obteniendo páginas:', error);
      mensaje = 'No se pudieron cargar las páginas.';
    }
  }

  async function cargarPaginaSeleccionada() {
  if (!paginaSeleccionadaId) {
    resetearCampos();
    return;
  }

  console.log("ID:", paginaSeleccionadaId);

  try {
    const res = await fetch(`http://127.0.0.1:8000/estructura_web/por-id/${paginaSeleccionadaId}`);
    const data = await res.json();
      resetearCampos();

      paginaSeleccionada = data.titulo;

      if (paginaSeleccionada === "Header y Footer") {
        nombre_seguro = data.nombre_seguro || '';
        logo = data.logo || '';
        footer = data.footer || '';
      } else {
        contenido = data.contenido || '';
      }

      mensaje = '';
    } catch (error) {
      console.error('Error cargando la página:', error);
      mensaje = 'Error al cargar la página seleccionada.';
    }
  }

  async function guardarCambios() {
    if (!paginaSeleccionadaId) {
      mensaje = 'Debes seleccionar una página para editar.';
      return;
    }

    const esHeaderFooter = paginaSeleccionada === "Header y Footer";

    const body = esHeaderFooter
      ? {
          nombre_seguro: nombre_seguro.trim(),
          logo: logo.trim(),
          footer: footer.trim()
        }
      : {
          contenido: contenido.trim()
        };

    const endpoint = `http://127.0.0.1:8000/estructura_web/actualizar/${paginaSeleccionadaId}`;

    try {
      const res = await fetch(endpoint, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });

      const result = await res.json();
      console.log("Respuesta del backend:", result);

      if (res.ok) {
        mensaje = result.message || 'Página actualizada correctamente.';
        await obtenerPaginas();
      } else {
        console.error('Error en respuesta:', result);
        mensaje = result.detail || 'Error al actualizar la página.';
      }
    } catch (error) {
      console.error('Error guardando cambios:', error);
      mensaje = 'Error inesperado al guardar cambios.';
    }
  }

  function cancelarEdicion() {
    paginaSeleccionada = '';
    paginaSeleccionadaId = '';
    resetearCampos();
    mensaje = '';
  }

  function resetearCampos() {
    contenido = '';
    nombre_seguro = '';
    logo = '';
    footer = '';
  }
</script>

<main>
  <h1>Editar Páginas</h1>

  <label for="paginaSelect">Selecciona la página:</label>
  <select id="paginaSelect" bind:value={paginaSeleccionadaId} on:change={cargarPaginaSeleccionada}>
    <option disabled selected value="">-- Elige una página --</option>
    {#each paginas as pagina}
      <option value={pagina._id}>{pagina.titulo}</option>
    {/each}
  </select>

  {#if paginaSeleccionada}
    <div class="formulario">
      <label><strong>Título:</strong> {paginaSeleccionada}</label>

      {#if paginaSeleccionada === "Header y Footer"}
        <label>Nombre Seguro (Header)</label>
        <input type="text" bind:value={nombre_seguro} />

        <label>Logo (URL)</label>
        <input type="text" bind:value={logo} />

        <label>Footer (Texto)</label>
        <textarea rows="3" bind:value={footer}></textarea>
      {:else}
        <label>Contenido</label>
        <textarea rows="6" bind:value={contenido}></textarea>
      {/if}

      <div class="acciones">
        <button on:click={guardarCambios}>Guardar Cambios</button>
        <button on:click={cancelarEdicion}>Cancelar</button>
      </div>
    </div>
  {/if}

  {#if mensaje}
    <p class="mensaje">{mensaje}</p>
  {/if}
</main>

<style>
  main { padding: 2rem; }
  label { display: block; margin-top: 1rem; font-weight: bold; }
  select, input, textarea { width: 100%; padding: 0.5rem; margin-top: 0.5rem; }
  .formulario { margin-top: 2rem; background: #f9f9f9; padding: 1rem; border-radius: 8px; max-width: 600px; }
  .acciones { margin-top: 1rem; }
  .acciones button { margin-right: 1rem; }
  .mensaje { margin-top: 1rem; color: green; font-weight: bold; }
</style>
