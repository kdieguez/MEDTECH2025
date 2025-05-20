<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  
  let footerTexto = '';

  onMount(() => {
    axios.get('http://127.0.0.1:8000/estructura_web/por-id/67d655a2f9a7d53085c7359d')
      .then(res => {
        const data = res.data;
        footerTexto = data.footer;
      })
      .catch(error => {
        console.error("Error cargando datos del Footer:", error);
        footerTexto = 'Información de footer sin cargar';
      });
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
    border-top: 10px solid #ffd000; 
  }
</style>
