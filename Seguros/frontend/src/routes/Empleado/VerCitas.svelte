<script>
  import flatpickr from "flatpickr";
  import "flatpickr/dist/flatpickr.css";
  import Swal from "sweetalert2";
  import { onMount } from "svelte";
  import { API_BASE_URL } from '../../lib/api';
  
  let citas = [];
  let fechaSeleccionada = "";
  let citasDelDia = [];
  let fechasConCitas = new Set();

  let citaEditando = null;
  let nuevaFechaHora = "";
  let nuevaSubcategoria = "";
  let subcategorias = [];

  onMount(async () => {
    await cargarCitas();

    const resSub = await fetch(`${API_BASE_URL}/servicios`);
    const datos = await resSub.json();
    subcategorias = datos.map(d => ({
      nombre_subcategoria: d.nombre_subcategoria,
      nombre_servicio: d.nombre_servicio,
      id_subcategoria: d.id_subcategoria
    }));

    flatpickr("#calendario-ver", {
      dateFormat: "Y-m-d",
      inline: true,
      minDate: "2020-01-01",
      onDayCreate: function (_, __, ___, dayElem) {
        const date = dayElem.dateObj.toISOString().split("T")[0];
        if (fechasConCitas.has(date)) {
          dayElem.style.backgroundColor = "#ffdddd";
          dayElem.style.borderRadius = "50%";
        }
      },
      onChange: function (_, dateStr) {
        fechaSeleccionada = dateStr;
        citasDelDia = citas.filter(c => c.fechaHora.startsWith(dateStr));
      }
    });
  });

  async function cargarCitas() {
    const res = await fetch(`${API_BASE_URL}/citas`);
    citas = await res.json();
    fechasConCitas = new Set(citas.map(c => c.fechaHora.split("T")[0]));
    citasDelDia = fechaSeleccionada
      ? citas.filter(c => c.fechaHora.startsWith(fechaSeleccionada))
      : [];
  }

  async function eliminarCita(cita) {
    const confirmacion = await Swal.fire({
      title: "¿Eliminar cita?",
      text: "Esta acción la eliminará también del hospital.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar",
      cancelButtonText: "Cancelar"
    });

    if (confirmacion.isConfirmed) {
      try {
        const res = await fetch(`${API_BASE_URL}/citas/${cita._id}`, {
          method: "DELETE"
        });

        const data = await res.json();
        if (res.ok) {
          Swal.fire("Eliminado", data.mensaje, "success");
          await cargarCitas();
        } else {
          Swal.fire("Error", data.detail || "No se pudo eliminar la cita", "error");
        }
      } catch (e) {
        Swal.fire("Error", "No se pudo conectar con el servidor", "error");
      }
    }
  }

  function abrirModalEdicion(cita) {
    citaEditando = cita;
    nuevaFechaHora = cita.fechaHora.slice(0, 16);
    nuevaSubcategoria = cita.subcategoria;
  }

  async function guardarEdicion() {
    const sub = subcategorias.find(s => s.nombre_subcategoria === nuevaSubcategoria);
    if (!sub) {
      Swal.fire("Error", "Subcategoría no válida", "error");
      return;
    }

    try {
      const res = await fetch(`${API_BASE_URL}/citas/${citaEditando._id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nuevaFechaHora: nuevaFechaHora,
          nuevaSubcategoria: sub.nombre_subcategoria,
          nuevoServicio: sub.nombre_servicio
        })
      });

      const data = await res.json();
      if (res.ok) {
        Swal.fire("Actualizado", data.mensaje, "success");
        citaEditando = null;
        await cargarCitas();
      } else {
        Swal.fire("Error", data.detail || "No se pudo actualizar", "error");
      }
    } catch (e) {
      Swal.fire("Error", "No se pudo conectar con el servidor", "error");
    }
  }
</script>

<div class="contenedor-ver">
  <h2>Ver Citas Registradas</h2>

  <div class="contenedor-flex">
    <div>
      <input id="calendario-ver" type="text" placeholder="Selecciona una fecha" />
    </div>

    <div class="citas-listado">
      {#if fechaSeleccionada}
        <h3>Citas para {fechaSeleccionada}</h3>
        {#if citasDelDia.length > 0}
          <ul>
            {#each citasDelDia as cita}
              <li>
                {cita.fechaHora.slice(11, 16)} - 
                {cita.servicio || 'Servicio'} - 
                {cita.subcategoria || 'Subcategoría'} - 
                {cita.nombrePaciente || 'Paciente'} 
                <span class="hospital">en {cita.hospital}</span>
                <button on:click={() => eliminarCita(cita)} class="btn-eliminar">🗑️</button>
                <button on:click={() => abrirModalEdicion(cita)} class="btn-editar">✏️</button>
              </li>
            {/each}
          </ul>
        {:else}
          <p>No hay citas para esta fecha.</p>
        {/if}
      {/if}
    </div>
  </div>
</div>

{#if citaEditando}
  <div class="modal-editar">
    <h3>Editar Cita</h3>
    <label>Fecha y Hora:</label>
    <input type="datetime-local" bind:value={nuevaFechaHora} />

    <label>Subcategoría:</label>
    <select bind:value={nuevaSubcategoria}>
      {#each subcategorias as sub}
        <option value={sub.nombre_subcategoria}>
  {sub.nombre_servicio} - {sub.nombre_subcategoria}
</option>

      {/each}
    </select>

    <div class="botones-modal">
      <button on:click={guardarEdicion}>Guardar</button>
      <button on:click={() => citaEditando = null}>Cancelar</button>
    </div>
  </div>
{/if}

<style>
  .contenedor-ver {
    text-align: center;
    margin-top: 30px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }

  .contenedor-flex {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    gap: 40px;
    margin-top: 20px;
    flex-wrap: wrap;
  }

  .citas-listado {
    max-width: 400px;
    text-align: left;
  }

  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    background-color: #eef5ff;
    padding: 12px;
    border-radius: 6px;
    margin-bottom: 10px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    font-size: 15px;
    line-height: 1.4;
    position: relative;
  }

  .hospital {
    font-weight: bold;
    color: #003366;
  }

  .btn-eliminar {
    background: none;
    border: none;
    cursor: pointer;
    float: right;
    font-size: 16px;
    color: #cc0000;
  }

  .btn-editar {
    background: none;
    border: none;
    cursor: pointer;
    float: right;
    font-size: 16px;
    color: #0077b6;
    margin-right: 8px;
  }

  input[type="text"] {
    padding: 10px;
    width: 250px;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-top: 10px;
  }

  h2, h3 {
    color: #003366;
  }

  p {
    margin-top: 10px;
    color: #cc0000;
    font-weight: bold;
  }

  .modal-editar {
    position: fixed;
    top: 20%;
    left: 50%;
    transform: translate(-50%, 0);
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 0 10px #ccc;
    z-index: 999;
    width: 300px;
  }

  .modal-editar input,
  .modal-editar select {
    width: 100%;
    padding: 8px;
    margin-top: 10px;
    margin-bottom: 15px;
    border-radius: 5px;
    border: 1px solid #ccc;
  }

  .botones-modal {
    display: flex;
    justify-content: space-between;
  }

  .botones-modal button {
    padding: 6px 12px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }

  .botones-modal button:first-child {
    background-color: #0077b6;
    color: white;
  }

  .botones-modal button:last-child {
    background-color: #ccc;
    color: black;
  }
</style>
