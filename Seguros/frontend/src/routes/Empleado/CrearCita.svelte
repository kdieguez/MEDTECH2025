<script>
  import flatpickr from "flatpickr";
  import "flatpickr/dist/flatpickr.css";
  import { onMount } from "svelte";

  let fechaSeleccionada = "";
  let hospitales = [];
  let servicios = [];
  let selectedHour = "";
  let selectedHospital = "";
  let selectedServicio = "";
  let idAfiliado = "";
  let mensaje = "";

  const horasPermitidas = [
    "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
    "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
    "14:00", "14:30", "15:00", "15:30", "16:00", "16:30"
  ];

  onMount(async () => {
    flatpickr("#calendario-crear", {
      dateFormat: "Y-m-d",
      minDate: "today",
      onChange: function(_, dateStr) {
        fechaSeleccionada = dateStr;
        mensaje = '';
        selectedHour = '';
      }
    });

    const h = await fetch("http://localhost:8000/hospitales");
    hospitales = await h.json();

    const s = await fetch("http://localhost:8000/servicios");
    servicios = await s.json();
  });

  function esHoy(fechaStr) {
    const hoy = new Date().toISOString().split("T")[0];
    return fechaStr === hoy;
  }

  function horaEsFutura(horaStr) {
    const ahora = new Date();
    const [hh, mm] = horaStr.split(":").map(Number);
    const horaCita = new Date();
    horaCita.setHours(hh, mm, 0, 0);
    return horaCita > ahora;
  }

  async function agendarCita() {
    if (!fechaSeleccionada || !selectedHour || !selectedHospital || !selectedServicio || !idAfiliado) {
      mensaje = 'Todos los campos son obligatorios';
      return;
    }

    const fechaCompleta = `${fechaSeleccionada}T${selectedHour}:00`;
const [id_subcategoria, id_servicio] = selectedServicio.split("|");
const servicioSeleccionado = servicios.find(
  s =>
    String(s.id_subcategoria) === String(id_subcategoria) &&
    String(s.id_servicio) === String(id_servicio)
);


const cita = {
  fechaHora: fechaCompleta,
  hospital: selectedHospital,
  servicio: {
    id_subcategoria,
    id_servicio,
    nombre_subcategoria: servicioSeleccionado?.nombre_subcategoria || "",
    nombre_servicio: servicioSeleccionado?.nombre_servicio || ""
  },
  id_afiliado: idAfiliado
};

console.log("Servicio seleccionado:", servicioSeleccionado);
console.log("Subcategoría enviada:", servicioSeleccionado?.nombre_subcategoria);


    const res = await fetch('http://localhost:8000/citas', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cita)
    });

    if (res.ok) {
      mensaje = "Cita creada correctamente.";
    } else {
      const error = await res.json();
      mensaje = error.detail || 'Error al crear la cita.';
    }
  }
</script>

<div class="contenedor-crear">
  <h2>Crear nueva cita</h2>
  <input id="calendario-crear" type="text" placeholder="Selecciona una fecha" />

  {#if fechaSeleccionada}
    <label for="afiliado">Afiliado</label>
    <input id="afiliado" type="text" bind:value={idAfiliado} placeholder="Código afiliado" />

    <label for="hora">Hora</label>
    <select id="hora" bind:value={selectedHour}>
      <option value="">Seleccione una hora</option>
      {#each horasPermitidas.filter(h => !esHoy(fechaSeleccionada) || horaEsFutura(h)) as h}
        <option value={h}>{h}</option>
      {/each}
    </select>

    <label for="hospital">Hospital</label>
    <select id="hospital" bind:value={selectedHospital}>
      <option value="">Seleccione un hospital</option>
      {#each hospitales as h}
        <option value={h.nombre}>{h.nombre}</option>
      {/each}
    </select>

    <label for="servicio">Servicio</label>
    <select id="servicio" bind:value={selectedServicio}>
      <option value="">Seleccione un servicio</option>
      {#each servicios as s}
        <option value={`${s.id_subcategoria}|${s.id_servicio}`}>
          {s.nombre_servicio} - {s.nombre_subcategoria}
        </option>
      {/each}
    </select>

    <button on:click={agendarCita}>Crear cita</button>
    {#if mensaje}
      <p>{mensaje}</p>
    {/if}
  {/if}
</div>

<style>
  .contenedor-crear {
    text-align: center;
    margin-top: 30px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    padding-bottom: 60px;
  }

  input[type="text"], select {
    padding: 8px;
    margin: 8px 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    width: 250px;
  }

  label {
    font-weight: bold;
    display: block;
    margin-top: 15px;
    text-align: left;
    width: 250px;
    margin-left: auto;
    margin-right: auto;
  }

  button {
    display: block;
    background-color: #003366;
    color: white;
    border: none;
    padding: 10px 20px;
    margin: 30px auto 0 auto;
    font-size: 14px;
    border-radius: 5px;
    cursor: pointer;
  }

  button:hover {
    background-color: #00509e;
  }

  h2 {
    color: #003366;
  }

  p {
    margin-top: 15px;
    color: #cc0000;
    font-weight: bold;
  }

  #servicio {
    margin-bottom: 20px;
  }
</style>
