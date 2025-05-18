<script>
  import flatpickr from "flatpickr";
  import "flatpickr/dist/flatpickr.css";
  import { onMount } from "svelte";

  let citas = [];
  let fechaSeleccionada = "";
  let citasDelDia = [];
  let fechasConCitas = new Set();

  onMount(async () => {
    const res = await fetch("http://localhost:8000/citas");
    citas = await res.json();

    fechasConCitas = new Set(citas.map(c => c.fecha_hora.split("T")[0]));

    flatpickr("#calendario-ver", {
  dateFormat: "Y-m-d",
  inline: true,
  minDate: "2020-01-01",
  onDayCreate: function(dObj, dStr, fp, dayElem) {
    const date = dayElem.dateObj.toISOString().split("T")[0];
    if (fechasConCitas.has(date)) {
      dayElem.style.backgroundColor = "#ffdddd";
      dayElem.style.borderRadius = "50%";
    }
  },
  onChange: function(selectedDates, dateStr) {
    fechaSeleccionada = dateStr;
    citasDelDia = citas.filter(c => c.fecha_hora.startsWith(dateStr));
  }
});

  });
</script>

<div class="contenedor-ver">
  <h2>Ver Citas Registradas</h2>
  <input id="calendario-ver" type="text" placeholder="Selecciona una fecha" />

  {#if fechaSeleccionada}
    <h3>Citas para {fechaSeleccionada}</h3>
    {#if citasDelDia.length > 0}
      <ul>
        {#each citasDelDia as cita}
          <li>{cita.fecha_hora.slice(11, 16)} - {cita.servicio} en {cita.hospital}</li>
        {/each}
      </ul>
    {:else}
      <p>No hay citas para esta fecha.</p>
    {/if}
  {/if}
</div>

<style>
  .contenedor-ver {
    text-align: center;
    margin-top: 30px;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  }

  ul {
    list-style-type: none;
    padding: 0;
    max-width: 500px;
    margin: 20px auto;
  }

  li {
    background-color: #eef5ff;
    padding: 10px;
    border-radius: 6px;
    margin-bottom: 8px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.1);
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
</style>
