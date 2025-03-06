<script>
  import { onMount } from "svelte";
  import { navigate } from "svelte-routing";

  let email = "";
  let password = "";
  let errorMessage = "";

  async function login() {
    errorMessage = ""; // Limpiar errores anteriores

    const response = await fetch("http://localhost:8000/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (response.ok) {
      localStorage.setItem("token", data.token);
      navigate("/"); // Redirigir al home
    } else {
      errorMessage = data.error || "Error en el inicio de sesión";
    }
  }
</script>

<div class="login-container">
  <h2>Iniciar Sesión</h2>
  {#if errorMessage}
    <p class="error">{errorMessage}</p>
  {/if}
  <form on:submit|preventDefault={login}>
    <label for="email">Email:</label>
    <input id="email" type="email" bind:value={email} required />

    <label for="password">Contraseña:</label>
    <input id="password" type="password" bind:value={password} required />

    <button type="submit">Ingresar</button>
  </form>
</div>

<style>
  .login-container {
    width: 300px;
    margin: auto;
    text-align: center;
  }
  .error {
    color: red;
  }
  input {
    display: block;
    width: 100%;
    margin-bottom: 10px;
  }
</style>
