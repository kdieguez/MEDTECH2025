<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import Swal from 'sweetalert2';
  import { API_BASE_URL } from '../../lib/api';

  let drafts = [];
  let draftSeleccionado = null;
  let comentario = '';

  onMount(() => {
    cargarDrafts();
  });

  function cargarDrafts() {
    axios.get(`${API_BASE_URL}/estructura_web/drafts-pendientes`)
      .then(res => drafts = res.data)
      .catch(() => Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudieron cargar los drafts' }));
  }

  function seleccionarDraft(id) {
    axios.get(`${API_BASE_URL}/estructura_web/draft/${id}`)
      .then(res => {
        draftSeleccionado = res.data;
        comentario = '';
      })
      .catch(() => Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo cargar el draft' }));
  }

  function moderarDraft(accion) {
    if (!draftSeleccionado) return;

    axios.post(`${API_BASE_URL}/estructura_web/moderar/${draftSeleccionado._id}`, {
      accion,
      comentario
    })
    .then(res => {
      Swal.fire({ icon: 'success', title: 'Listo', text: res.data.message });
      draftSeleccionado = null;
      cargarDrafts();
    })
    .catch(err => {
      const msg = err.response?.data?.detail || 'Error al procesar la moderación';
      Swal.fire({ icon: 'error', title: 'Error', text: msg });
    });
  }
</script>

<main class="moderacion">
  <h1>Moderación de Contenido</h1>

  {#if !draftSeleccionado}
    <h2>Drafts Pendientes</h2>
    {#if drafts.length > 0}
      <ul>
        {#each drafts as draft}
          <li>
            <div>
              <strong>{draft.contenido?.titulo || 'Sin título'}</strong> – Página ID: {draft.id_pagina}
              <br />
              <small>Autor: {draft.autor || 'desconocido'}</small>
            </div>
            <button on:click={() => seleccionarDraft(draft._id)}>Ver</button>
          </li>
        {/each}
      </ul>
    {:else}
      <p>No hay cambios pendientes.</p>
    {/if}
  {:else}
    <div class="draft-preview">
      <h2>Vista Previa del Draft</h2>
      <p><strong>ID Página:</strong> {draftSeleccionado.id_pagina}</p>
      <p><strong>Fecha:</strong> {new Date(draftSeleccionado.fecha).toLocaleString()}</p>
      <p><strong>Autor del cambio:</strong> {draftSeleccionado.autor || 'desconocido'}</p>

      <div class="contenido-preview">
        <pre>{JSON.stringify(draftSeleccionado.contenido, null, 2)}</pre>
      </div>

      <div class="comentario">
        <label>Comentario del administrador (opcional si se rechaza):</label>
        <textarea rows="3" bind:value={comentario}></textarea>
      </div>

      <div class="botones">
        <button on:click={() => moderarDraft('aprobar')} class="aprobar">Aprobar</button>
        <button on:click={() => moderarDraft('rechazar')} class="rechazar">Rechazar</button>
        <button on:click={() => draftSeleccionado = null} class="cancelar">Cancelar</button>
      </div>
    </div>
  {/if}
</main>

<style>
  .moderacion {
    max-width: 900px;
    margin: auto;
    padding: 2rem;
    font-family: Arial, sans-serif;
  }

  h1, h2 {
    text-align: center;
    color: #1c2c40;
  }

  ul {
    list-style: none;
    padding: 0;
  }

  li {
    background: #f9f9f9;
    margin-bottom: 1rem;
    padding: 1rem;
    border-radius: 8px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  li button {
    background-color: #0077b6;
    color: white;
    padding: 0.4rem 1rem;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .draft-preview {
    background: #fdfdfd;
    padding: 2rem;
    border-radius: 8px;
    border: 1px solid #ccc;
  }

  .contenido-preview pre {
    background: #f0f0f0;
    padding: 1rem;
    overflow-x: auto;
    border-radius: 6px;
    max-height: 400px;
  }

  .comentario {
    margin-top: 1rem;
  }

  textarea {
    width: 100%;
    padding: 0.6rem;
    border-radius: 6px;
    border: 1px solid #ccc;
  }

  .botones {
    display: flex;
    justify-content: center;
    gap: 1rem;
    margin-top: 1.5rem;
  }

  .botones button {
    padding: 0.6rem 1.4rem;
    font-weight: bold;
    border: none;
    border-radius: 10px;
    cursor: pointer;
  }

  .aprobar {
    background-color: #28a745;
    color: white;
  }

  .rechazar {
    background-color: #dc3545;
    color: white;
  }

  .cancelar {
    background-color: #aaa;
    color: white;
  }
</style>