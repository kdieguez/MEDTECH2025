<script>
  const irAHospitales = () => {
    window.location.href = 'https://hospitales.medtech.com';
  };

  const irASeguros = () => {
    window.location.href = 'https://seguros.medtech.com';
  };
</script>

<style>
  :global(body, html) {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background-color: #E2D4FC;
  }

  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: auto;
    height: auto;
    background-color: #E2D4FC;
    color: #1f2937;
    padding: 0;
  }

  h1 {
    font-size: 3rem;
    margin-bottom: 2rem;
    color: #FF9800;
  }

  .options {
    display: flex;
    gap: 2rem;
    flex-wrap: wrap;
    justify-content: center;
  }

  .card {
    background-color: #ffffff;
    color: #1f2937;
    width: 250px;
    height: 250px;
    border-radius: 16px;
    box-shadow: 0 10px 15px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: transform 0.3s, box-shadow 0.3s;
    border: 3px solid #FF9800;
  }

  .card:hover {
    transform: translateY(-10px);
    box-shadow: 0 15px 25px rgba(0, 0, 0, 0.3);
  }

  .card h2 {
    font-size: 1.5rem;
    margin-bottom: 1rem;
  }

  .card p {
    text-align: center;
    font-size: 1rem;
    padding: 0 10px;
  }
</style>

<div class="container">
  <h1>Bienvenido a MEDTECH</h1>
  <div class="options">
    <div class="card" on:click={irAHospitales}>
      <h2>Hospitales</h2>
      <p>Accede al sistema de gestión hospitalaria.</p>
    </div>

    <div class="card" on:click={irASeguros}>
      <h2>Seguros</h2>
      <p>Accede al sistema de administración de seguros.</p>
    </div>
  </div>
</div>