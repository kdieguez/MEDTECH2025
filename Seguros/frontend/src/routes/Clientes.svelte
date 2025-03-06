<script>
  import { onMount } from "svelte";
  import { navigate } from "svelte-routing";

  let clientes = [];

  onMount(async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/login"); // Redirigir si no está logueado
      return;
    }

    const res = await fetch("http://localhost:8000/clientes", {
      headers: { "Authorization": `Bearer ${token}` }
    });
    clientes = await res.json();
  });
</script>

<h2>Clientes Registrados</h2>
<ul>
  {#each clientes as cliente}
    <li>{cliente.nombre} - {cliente.tipo_poliza}</li>
  {/each}
</ul>
