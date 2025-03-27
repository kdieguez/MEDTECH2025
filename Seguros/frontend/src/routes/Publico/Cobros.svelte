<script>
  import { onMount } from 'svelte';
  import axios from 'axios';

  let usuarios = [];
  let nombreBusqueda = '';

async function cargarUsuarios() {
  try {
    const response = await axios.get('http://127.0.0.1:8000/cobros/listar');
    usuarios = response.data.sort((a, b) => new Date(a.fecha_vencimiento) - new Date(b.fecha_vencimiento));
  } catch (error) {
    console.error('Error al cargar usuarios:', error);
    alert('No se pudieron cargar los usuarios.');
  }
}

async function renovar(usuarioId) {
  const nuevaFecha = prompt('Ingrese la nueva fecha de vencimiento (YYYY-MM-DD):');
  if (!nuevaFecha) return;

  const numeroTarjeta = prompt('Ingrese el número de tarjeta:');
  if (!numeroTarjeta) return;

  const cvv = prompt('Ingrese el CVV:');
  if (!cvv) return;

  try {
    await axios.put(`http://127.0.0.1:8000/cobros/renovar/${usuarioId}`, {
      nueva_fecha: nuevaFecha,
      numero_tarjeta: numeroTarjeta,
      cvv: cvv
    });
    alert('Póliza renovada.');
    await cargarUsuarios();
  } catch (error) {
    console.error('Error al renovar póliza:', error);
    alert(error.response?.data?.detail || 'No se pudo renovar la póliza.');
  }
}

  async function deshabilitar(usuarioId) {
    const confirmar = confirm('¿Estás seguro de que deseas deshabilitar este usuario?');
    if (!confirmar) return;

    try {
      await axios.put(`http://127.0.0.1:8000/cobros/deshabilitar/${usuarioId}`);
      alert('Usuario deshabilitado.');
      cargarUsuarios();
    } catch (error) {
      console.error('Error al deshabilitar usuario:', error);
      alert('No se pudo deshabilitar al usuario.');
    }
  }

  onMount(cargarUsuarios);
</script>

<style>
  .contenedor {
    max-width: 800px;
    margin: auto;
    padding: 20px;
  }

  .filtros {
    margin-bottom: 20px;
    display: flex;
    gap: 1rem;
  }

  input[type="text"] {
    padding: 8px;
    width: 100%;
    border: 1px solid #ccc;
    border-radius: 8px;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
  }

  th, td {
    padding: 12px;
    border-bottom: 1px solid #ddd;
    text-align: left;
  }

  th {
    background-color: #f4f4f4;
  }

  button {
    padding: 8px 12px;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    margin-right: 8px;
  }

  .renovar {
    background-color: #4caf50;
    color: white;
  }

  .deshabilitar {
    background-color: #f44336;
    color: white;
  }
</style>

<div class="contenedor">
  <h2>Módulo de Cobros</h2>

  <div class="filtros">
    <input
      type="text"
      placeholder="Buscar por nombre..."
      bind:value={nombreBusqueda}
    />
  </div>

  {#if usuarios.length === 0}
    <p>No hay usuarios con pólizas activas.</p>
  {:else}
    <table>
      <thead>
        <tr>
          <th>Nombre</th>
          <th>ID</th>
          <th>Fecha Vencimiento</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        {#each usuarios.filter(u => u.nombre?.toLowerCase().includes(nombreBusqueda.toLowerCase())) as usuario}
          <tr>
            <td>{usuario.nombre} {usuario.apellido}</td>
            <td>{usuario._id}</td>
            <td>{usuario.fecha_vencimiento?.split('T')[0]}</td>
            <td>
              <button class="renovar" on:click={() => renovar(usuario._id)}>Renovar</button>
              <button class="deshabilitar" on:click={() => deshabilitar(usuario._id)}>Deshabilitar</button>
            </td>
          </tr>
        {/each}
      </tbody>
    </table>
  {/if}
</div>