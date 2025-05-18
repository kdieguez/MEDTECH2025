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
    const resServicios = await axios.get('http://localhost:8000/servicios-hospitalarios');
    servicios = resServicios.data || [];

    const resCatalogo = await axios.get('http://localhost:8000/catalogo');
    const catalogo = resCatalogo.data || [];

    idsCatalogo = catalogo.map(item => item.servicio.id_subcategoria);

    // 🔁 Actualizar el precio del servicio si está en el catálogo
    for (let i = 0; i < servicios.length; i++) {
      const sub = servicios[i].id_subcategoria;
      const encontrado = catalogo.find(item => item.servicio.id_subcategoria === sub);
      if (encontrado) {
        servicios[i].precio = encontrado.servicio.precio; // Sobrescribe el precio con el de Mongo
      }
    }
  } catch (e) {
    Swal.fire("Error", "No se pudieron cargar los datos", "error");
    console.error(e);
  }
});


  async function agregarAlCatalogo(servicio) {
    const polizasSeleccionadas = seleccionadas[servicio.id_subcategoria] || [];

    if (polizasSeleccionadas.length === 0) {
      Swal.fire("Atención", "Debes seleccionar al menos una póliza.", "warning");
      return;
    }

    const payload = {
      servicio: {
        id_servicio: servicio.id_servicio,
        nombre_servicio: servicio.nombre_servicio,
        descripcion_servicio: servicio.descripcion_servicio,
        id_subcategoria: servicio.id_subcategoria,
        nombre_subcategoria: servicio.nombre_subcategoria,
        descripcion_subcategoria: servicio.descripcion_subcategoria,
        precio: servicio.precio,
        id_info_doctor: servicio.id_info_doctor[0] ?? null
      },
      disponible: true,
      tipos_poliza: polizasSeleccionadas
    };

    try {
      const response = await axios.post('http://localhost:8000/catalogo', payload);
      idsCatalogo.push(servicio.id_subcategoria);
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
        <h2>{s.nombre_subcategoria}</h2>
        <p><strong>Servicio:</strong> {s.nombre_servicio}</p>
        <p><strong>Descripción:</strong> {s.descripcion_subcategoria}</p>
        <p><strong>Precio:</strong>
  {#if idsCatalogo.includes(s.id_subcategoria)}
    Q{s.precio}
  {:else}
    <input
      type="number"
      bind:value={s.precio}
      min="0"
      class="precio-input"
    />
  {/if}
</p>



        <div>
          <strong>Selecciona póliza:</strong><br />
          {#each POLIZAS_DISPONIBLES as poliza}
            <label>
              <input
                type="checkbox"
                value={poliza}
                on:change={(e) => {
                  const selected = seleccionadas[s.id_subcategoria] || [];
                  if (e.target.checked) {
                    seleccionadas[s.id_subcategoria] = [...selected, poliza];
                  } else {
                    seleccionadas[s.id_subcategoria] = selected.filter(p => p !== poliza);
                  }
                }}
                checked={seleccionadas[s.id_subcategoria]?.includes(poliza)}
                disabled={idsCatalogo.includes(s.id_subcategoria)}
              />
              {poliza}
            </label>
          {/each}
        </div>

        {#if idsCatalogo.includes(s.id_subcategoria)}
          <button class="eliminar" on:click={() => eliminarDelCatalogo(s.id_subcategoria)}>
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