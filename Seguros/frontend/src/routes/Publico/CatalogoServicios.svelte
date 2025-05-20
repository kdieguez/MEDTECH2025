<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import { API_BASE_URL } from '../../lib/api';

  let serviciosCatalogo = [];
  let error = '';

  onMount(async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/catalogo`);
      serviciosCatalogo = res.data || [];
    } catch (e) {
      error = 'Error al cargar el catálogo de servicios.';
      console.error(e);
    }
  });
</script>

<div class="contenedor">
  <h1 class="titulo">Catálogo de Servicios Asegurados</h1>

  {#if error}
    <div class="error">{error}</div>
  {/if}

  <div class="lista-servicios">
    {#each serviciosCatalogo as item}
      <div class="servicio">
        <h2>{item.servicio.nombre_subcategoria}</h2>
        <p><strong>Servicio:</strong> {item.servicio.nombre_servicio}</p>
        <p><strong>Descripción:</strong> {item.servicio.descripcion_subcategoria}</p>
        <p><strong>Precio:</strong> Q{item.servicio.precio}</p>
        <p><strong>Pólizas aplicables:</strong>
          {#each item.tipos_poliza as p, i}
            <span>{p}{i < item.tipos_poliza.length - 1 ? ', ' : ''}</span>
          {/each}
        </p>
      </div>
    {/each}
  </div>
</div>

<style>
  .contenedor {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .titulo {
    margin: 30px 0 10px;
    text-align: center;
    font-size: 2rem;
    color: #444;
  }

  .lista-servicios {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;
    justify-content: center;
    padding: 20px;
  }

  .servicio {
    background-color: #d4defc;
    border: 2px solid #aebff2;
    padding: 20px;
    border-radius: 12px;
    max-width: 250px;
  }

  .error {
    color: red;
    font-weight: bold;
    margin-bottom: 15px;
    text-align: center;
  }

  span {
    font-weight: bold;
  }
</style>