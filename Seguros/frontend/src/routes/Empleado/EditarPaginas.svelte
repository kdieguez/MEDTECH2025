<script>
  import axios from 'axios';
  import { onMount } from 'svelte';
  import Swal from 'sweetalert2';

  let paginas = [];
  let paginaSeleccionada = '';
  let paginaSeleccionadaId = '';
  let contenido = '';
  let nombre_seguro = '';
  let logo = '';
  let footer = '';
  let mensaje = '';
  
  onMount(() => {
    obtenerPaginas();
  });

  function obtenerPaginas() {
    axios.get('http://127.0.0.1:8000/estructura_web')
      .then(response => {
        paginas = response.data;
      })
      .catch(error => {
        console.error('Error obteniendo páginas:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las páginas.'
        });
      });
  }

  function cargarPaginaSeleccionada() {
    if (!paginaSeleccionadaId) {
      resetearCampos();
      return;
    }

    axios.get(`http://127.0.0.1:8000/estructura_web/por-id/${paginaSeleccionadaId}`)
      .then(response => {
        const data = response.data;

        resetearCampos();

        paginaSeleccionada = data.titulo;

        if (paginaSeleccionada === "Header y Footer") {
          nombre_seguro = data.nombre_seguro || '';
          logo = data.logo || '';
          footer = data.footer || '';
        } else {
          contenido = data.contenido || '';
        }

      })
      .catch(error => {
        console.error('Error cargando la página:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error al cargar la página seleccionada.'
        });
      });
  }

  function guardarCambios() {
    if (!paginaSeleccionadaId) {
      Swal.fire({
        icon: 'warning',
        title: 'Atención',
        text: 'Debes seleccionar una página para editar.'
      });
      return;
    }

    const esHeaderFooter = (paginaSeleccionada === "Header y Footer");

    const body = esHeaderFooter
      ? {
          nombre_seguro: nombre_seguro.trim(),
          logo: logo.trim(),
          footer: footer.trim()
        }
      : {
          contenido: contenido.trim()
        };

    const endpoint = esHeaderFooter
      ? `http://127.0.0.1:8000/estructura_web/actualizar-header-footer/${paginaSeleccionadaId}`
      : `http://127.0.0.1:8000/estructura_web/actualizar-contenido/${paginaSeleccionadaId}`;

    axios.put(endpoint, body, {
      headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
      const result = response.data;

      Swal.fire({
        icon: 'success',
        title: '¡Éxito!',
        text: result.message || 'Página actualizada correctamente.'
      });

      obtenerPaginas(); // Recargar la lista
    })
    .catch(error => {
      console.error('Error guardando cambios:', error);

      let mensaje = 'Error inesperado al guardar cambios.';
      if (error.response) {
        mensaje = error.response.data.detail || mensaje;
      }

      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: mensaje
      });
    });
  }

  function cancelarEdicion() {
    paginaSeleccionada = '';
    paginaSeleccionadaId = '';
    resetearCampos();
  }

  function resetearCampos() {
    contenido = '';
    nombre_seguro = '';
    logo = '';
    footer = '';
  }
</script>

<main class="pagina-editar">
  <h1>Editar Páginas Informativas</h1>
  <div class="selector-contenedor">
    <label for="paginaSelect">Selecciona la página:</label>
    <select id="paginaSelect" bind:value={paginaSeleccionadaId} on:change={cargarPaginaSeleccionada}>
      <option disabled selected value="">-- Elige una página --</option>
      {#each paginas as pagina}
        <option value={pagina._id}>{pagina.titulo}</option>
      {/each}
    </select>
  </div>

  {#if paginaSeleccionada}
    <div class="formulario-edicion">
      <h2>{paginaSeleccionada}</h2>

      {#if paginaSeleccionada === "Header y Footer"}
        <div class="campo">
          <label>Nombre del Seguro (Header):</label>
          <input type="text" bind:value={nombre_seguro} placeholder="Nombre del seguro" />
        </div>

        <div class="campo">
          <label>Logo (URL):</label>
          <input type="text" bind:value={logo} placeholder="URL del logo" />
        </div>

        <div class="campo">
          <label>Footer (Texto):</label>
          <textarea rows="3" bind:value={footer} placeholder="Texto del footer"></textarea>
        </div>

      {:else}
        <div class="campo">
          <label>Contenido:</label>
          <textarea rows="6" bind:value={contenido} placeholder="Contenido de la página"></textarea>
        </div>

      {/if}

      <div class="acciones">
        <button class="btn guardar" on:click={guardarCambios}>Guardar Cambios</button>
        <button class="btn cancelar" on:click={cancelarEdicion}>Cancelar</button>
      </div>
    </div>
  {/if}

  {#if mensaje}
    <p class="mensaje-estado">{mensaje}</p>
  {/if}
</main>

<style>
.pagina-editar {
  max-width: 800px;
  font-family: Arial, sans-serif;
}

.pagina-editar h1 {
  text-align: center;
  color: #2c3e50;
  margin-bottom: 2rem;
}

.selector-contenedor {
  margin-bottom: 2rem;
}

.selector-contenedor label {
  font-weight: bold;
  display: block;
  margin-bottom: 0.5rem;
}

.selector-contenedor select {
  width: 100%;
  padding: 0.75rem;
  border: 2px solid #d4defc;
  border-radius: 8px;
  background-color: #ffffff;
  font-size: 1rem;
}

.formulario-edicion {
  background-color: #fdfdfd;
  border: 2px solid #d4defc;
  border-radius: 10px;
  padding: 2rem;
  padding-right: 3.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: 0.3s ease;
  margin-bottom: 80px;
}

.formulario-edicion:hover {
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.formulario-edicion h2 {
  margin-bottom: 1.5rem;
  color: #2c3e50;
  text-align: center;
}

.campo {
  margin-bottom: 1.5rem;
}

.campo label {
  display: block;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.campo input,
.campo textarea {
  width: 100%;
  padding: 0.75rem;
  border-radius: 8px;
  border: 1px solid #ccc;
  transition: border-color 0.3s ease;
}

.campo input:focus,
.campo textarea:focus {
  outline: none;
  border-color: #ffd000;
}

.acciones {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.btn {
  padding: 0.75rem 2rem;
  border-radius: 25px;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  border: none;
}

.btn.guardar {
  background-color: #ffd000;
  color: #2c3e50;
}

.btn.guardar:hover {
  background-color: #e6bc00;
  transform: translateY(-2px);
}

.btn.cancelar {
  background-color: #d4defc;
  color: #2c3e50;
}

.btn.cancelar:hover {
  background-color: #b3c7fa;
  transform: translateY(-2px);
}

.mensaje-estado {
  margin-top: 2rem;
  text-align: center;
  color: green;
  font-weight: bold;
}

@media (max-width: 600px) {
  .pagina-editar {
    padding: 1rem;
  }

  .acciones {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
