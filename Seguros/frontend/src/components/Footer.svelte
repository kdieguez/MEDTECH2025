<script>
  import { onMount } from 'svelte';

  let footerTexto;

  onMount(async () => {
    try {
      const res = await fetch('http://127.0.0.1:8000/estructura_web/por-titulo/Header y Footer');

      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }

      const data = await res.json();
      footerTexto = data.footer;
    } catch (error) {
      console.error("Error cargando datos del Footer:", error);
      footerTexto = 'Información de footer sin cargar';
    }
  });
</script>

<footer class="footer">
  <p>{footerTexto}</p>
</footer>

<style>
  .footer {
    background-color: #d4defc;
    color: black;
    text-align: center;
    padding: 10px;
    position: fixed;
    bottom: 0;
    left: 0;
    width: 100%;
    border-top: 10px solid #fffd59; 
  }
</style>
