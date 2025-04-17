<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import Swal from 'sweetalert2';

  let servicios = [];
  let idsCatalogo = [];
  const POLIZAS_DISPONIBLES = ["70%", "90%"];
  let seleccionadas = {};

  onMount(async () => {
    try {
      const res = await axios.get('http://localhost:8000/servicios-hospitalarios');
      servicios = res.data.servicios || [];

      const resCatalogo = await axios.get('http://localhost:8000/catalogo');
      idsCatalogo = resCatalogo.data.map(item => item.servicio.id_subcategoria);
    } catch (e) {
      Swal.fire("Error", "No se pudieron cargar los datos", "error");
      console.error(e);
    }
  });

  async function agregarAlCatalogo(servicio) {
    const polizasSeleccionadas = seleccionadas[servicio.ID_SUBCATEGORIA] || [];

    if (polizasSeleccionadas.length === 0) {
      Swal.fire("Atención", "Debes seleccionar al menos una póliza.", "warning");
      return;
    }

    const payload = {
      servicio: {
        id_servicio: servicio.ID_SERVICIO,
        nombre_servicio: servicio.NOMBRE_SERVICIO,
        descripcion_servicio: servicio.DESCRIPCION_SERVICIO,
        id_subcategoria: servicio.ID_SUBCATEGORIA,
        nombre_subcategoria: servicio.NOMBRE_SUBCATEGORIA,
        descripcion_subcategoria: servicio.DESCRIPCION_SUBCATEGORIA,
        precio: servicio.PRECIO,
        id_info_doctor: servicio.ID_INFO_DOCTOR[0] ?? null
      },
      disponible: true,
      tipos_poliza: polizasSeleccionadas
    };

    try {
      const response = await axios.post('http://localhost:8000/catalogo', payload);
      idsCatalogo.push(servicio.ID_SUBCATEGORIA);
      Swal.fire("Éxito", response.data.message, "success");
    } catch (e) {
      Swal.fire("Error", "Error al agregar al catálogo", "error");
      console.error(e);
    }
  }

  async function eliminarDelCatalogo(id_subcategoria) {
    const confirmacion = await Swal.fire({
      title: "¿Estás segura?",
      text: "El servicio será eliminado del catálogo",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    });

    if (!confirmacion.isConfirmed) return;

    try {
      const response = await axios.delete(`http://localhost:8000/catalogo/${id_subcategoria}`);
      idsCatalogo = idsCatalogo.filter(id => id !== id_subcategoria);
      Swal.fire("Eliminado", response.data.message, "success");
    } catch (e) {
      Swal.fire("Error", "No se pudo eliminar del catálogo", "error");
      console.error(e);
    }
  }
</script>

<main>
  <h1>Servicios Hospitalarios</h1>

  <div class="contenedor-servicios">
    {#each servicios as s}
      <div class="servicio">
        <h2>{s.NOMBRE_SUBCATEGORIA}</h2>
        <p><strong>Servicio:</strong> {s.NOMBRE_SERVICIO}</p>
        <p><strong>Descripción:</strong> {s.DESCRIPCION_SUBCATEGORIA}</p>
        <p><strong>Precio:</strong> Q{s.PRECIO}</p>

        <div>
          <strong>Selecciona póliza:</strong><br />
          {#each POLIZAS_DISPONIBLES as poliza}
            <label>
              <input
                type="checkbox"
                value={poliza}
                on:change={(e) => {
                  const selected = seleccionadas[s.ID_SUBCATEGORIA] || [];
                  if (e.target.checked) {
                    seleccionadas[s.ID_SUBCATEGORIA] = [...selected, poliza];
                  } else {
                    seleccionadas[s.ID_SUBCATEGORIA] = selected.filter(p => p !== poliza);
                  }
                }}
                checked={seleccionadas[s.ID_SUBCATEGORIA]?.includes(poliza)}
                disabled={idsCatalogo.includes(s.ID_SUBCATEGORIA)}
              />
              {poliza}
            </label>
          {/each}
        </div>

        {#if idsCatalogo.includes(s.ID_SUBCATEGORIA)}
          <button class="eliminar" on:click={() => eliminarDelCatalogo(s.ID_SUBCATEGORIA)}>
            🗑️ Eliminar del catálogo
          </button>
        {:else}
          <button on:click={() => agregarAlCatalogo(s)}>
            Agregar al catálogo
          </button>
        {/if}
      </div>
    {/each}
  </div>
</main>

<style>
  main {
    padding: 20px;
  }

  h1 {
    text-align: center;
    font-size: 2.2rem;
    margin-bottom: 30px;
    color: #222;
  }

  .contenedor-servicios {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 25px;
  }

  .servicio {
    background-color: #d4defc;
    border: 2px solid #aebff2;
    padding: 20px;
    border-radius: 16px;
    max-width: 280px;
    width: 100%;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    transition: transform 0.2s ease;
  }

  .servicio:hover {
    transform: scale(1.02);
  }

  label {
    margin-right: 10px;
    font-weight: 500;
  }

  button {
    margin-top: 14px;
    padding: 10px 20px;
    background-color: #fff659;
    color: #000;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.3s ease;
  }

  button:hover {
    background-color: #f5e84d;
  }

  button.eliminar {
    background-color: #f87171;
    color: white;
    margin-top: 16px;
  }

  button.eliminar:hover {
    background-color: #dc2626;
  }

  input[type="checkbox"] {
    margin-right: 6px;
  }

  @media (max-width: 768px) {
    .contenedor-servicios {
      flex-direction: column;
      align-items: center;
    }
  }
</style>
