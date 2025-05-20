<script>
  import { API_BASE_URL } from '../../lib/api';

  let nombre = "";
  let apellido = "";
  let email = "";
  let tipoPoliza = "90%";
  let mensaje = "";
  let error = "";

  async function registrarVenta() {
    mensaje = "";
    error = "";

    try {
      const res = await fetch(`${API_BASE_URL}/api/ventas`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nombre, apellido, email, tipoPoliza }),
      });

      const data = await res.json();

      if (!res.ok) {
        error = data.detail || "Error al registrar al usuario.";
        return;
      }

      mensaje = data.mensaje;
      nombre = "";
      apellido = "";
      email = "";
      tipoPoliza = "90%";
    } catch (err) {
      error = "Error de conexión con el servidor.";
    }
  }
</script>

<style>
  .form-container {
    max-width: 500px;
    margin: 0 auto;
    padding: 2rem;
    background: #f9f9f9;
    border-radius: 1rem;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  }

  input,
  select {
    width: 100%;
    padding: 0.75rem;
    margin-bottom: 1rem;
    border: 1px solid #ccc;
    border-radius: 0.5rem;
  }

  button {
    padding: 0.75rem 1.5rem;
    background-color: #2563eb;
    color: white;
    border: none;
    border-radius: 0.5rem;
    cursor: pointer;
  }

  button:hover {
    background-color: #1d4ed8;
  }

  .mensaje {
    margin-top: 1rem;
    color: green;
    font-weight: bold;
  }

  .error {
    margin-top: 1rem;
    color: red;
    font-weight: bold;
  }
</style>

<div class="form-container">
  <h1 class="text-2xl font-bold mb-4">Registrar Venta de Póliza</h1>

  <form on:submit|preventDefault={registrarVenta}>
    <input
      type="text"
      placeholder="Nombre"
      bind:value={nombre}
      required
    />

    <input
      type="text"
      placeholder="Apellido"
      bind:value={apellido}
      required
    />

    <input
      type="email"
      placeholder="Correo electrónico"
      bind:value={email}
      required
    />

    <select bind:value={tipoPoliza}>
      <option value="90%">Póliza 90%</option>
      <option value="70%">Póliza 70%</option>
    </select>

    <button type="submit">Registrar Venta</button>
  </form>

  {#if mensaje}
    <p class="mensaje">{mensaje}</p>
  {/if}

  {#if error}
    <p class="error">{error}</p>
  {/if}
</div>