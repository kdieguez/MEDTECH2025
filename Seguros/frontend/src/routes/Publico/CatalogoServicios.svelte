<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import Swal from 'sweetalert2';

  let servicios = [];
  let showModal = false;
  let editarServicio = null;

  let nombre = "";
  let descripcion = "";
  let imagen = "";
  let subcategorias = [];

  let subNombre = "";
  let subPrecio = "";
  let subPolizas = [];

  const opcionesPolizas = ["70%", "90%"];

  onMount(() => {
    obtenerServicios();
  });

  function obtenerServicios() {
    axios.get("http://127.0.0.1:8000/servicios/")
      .then(response => {
        servicios = response.data;
      })
      .catch(error => {
        console.error("Error al obtener servicios", error);
        Swal.fire("Error", "No se pudieron cargar los servicios", "error");
      });
  }

  function abrirModalCrear() {
    showModal = true;
    editarServicio = null;

    nombre = "";
    descripcion = "";
    imagen = "";
    subcategorias = [];
  }

  function abrirModalEditar(servicio) {
    showModal = true;
    editarServicio = servicio;

    nombre = servicio.nombre;
    descripcion = servicio.descripcion;
    imagen = servicio.imagen;
    subcategorias = [...servicio.subcategorias];
  }

  function agregarSubcategoria() {
    if (!subNombre || !subPrecio || subPolizas.length === 0) {
      Swal.fire("Atención", "Completa los campos de subcategoría", "warning");
      return;
    }

    subcategorias.push({
      nombre: subNombre,
      precio: parseFloat(subPrecio),
      polizas_cubren: [...subPolizas]
    });

    subNombre = "";
    subPrecio = "";
    subPolizas = [];

    Swal.fire("Agregado", "Subcategoría agregada correctamente", "success");
  }

  function eliminarSubcategoria(index) {
    subcategorias.splice(index, 1);
  }

  function guardarServicio() {
    if (!nombre || !descripcion || !imagen) {
      Swal.fire("Atención", "Completa los campos principales", "warning");
      return;
    }

    const data = {
      nombre,
      descripcion,
      imagen,
      subcategorias
    };

    if (editarServicio) {
      axios.put(`http://127.0.0.1:8000/servicios/${editarServicio._id}`, data)
        .then(() => {
          Swal.fire("Actualizado", "Servicio actualizado correctamente", "success");
          showModal = false;
          obtenerServicios();
        })
        .catch(() => {
          Swal.fire("Error", "No se pudo actualizar el servicio", "error");
        });
    } else {
      axios.post("http://127.0.0.1:8000/servicios/", data)
        .then(() => {
          Swal.fire("Creado", "Servicio creado correctamente", "success");
          showModal = false;
          obtenerServicios();
        })
        .catch(() => {
          Swal.fire("Error", "No se pudo crear el servicio", "error");
        });
    }
  }

  function eliminarServicio(id) {
    Swal.fire({
      title: "¿Estás seguro?",
      text: "Esto eliminará el servicio y sus subcategorías",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Sí, eliminar"
    }).then(result => {
      if (result.isConfirmed) {
        axios.delete(`http://127.0.0.1:8000/servicios/${id}`)
          .then(() => {
            Swal.fire("Eliminado", "Servicio eliminado correctamente", "success");
            obtenerServicios();
          })
          .catch(() => {
            Swal.fire("Error", "No se pudo eliminar el servicio", "error");
          });
      }
    });
  }
</script>

<h2>Catálogo de Servicios</h2>
<button on:click={abrirModalCrear} class="btn-crear">➕</button>

<div class="servicios-container">
  {#each servicios as servicio}
    <div class="servicio-card">
      <img src={servicio.imagen} alt={servicio.nombre} />
      <h3>{servicio.nombre}</h3>
      <p>{servicio.descripcion}</p>

      <h4>Subcategorías:</h4>
      <ul>
        {#each servicio.subcategorias as sub}
          <li>
            <strong>{sub.nombre}</strong> - Q{sub.precio}
            <br />
            <em>Cubre: {sub.polizas_cubren.join(", ")}</em>
          </li>
        {/each}
      </ul>

      <div class="acciones">
        <button on:click={() => abrirModalEditar(servicio)}>✏️ Editar</button>
        <button on:click={() => eliminarServicio(servicio._id)}>🗑️ Eliminar</button>
      </div>
    </div>
  {/each}
</div>

{#if showModal}
  <div class="modal">
    <div class="modal-content">
      <h2>{editarServicio ? "Editar Servicio" : "Nuevo Servicio"}</h2>

      <label>Nombre del Servicio</label>
      <input type="text" bind:value={nombre} />

      <label>Descripción</label>
      <input type="text" bind:value={descripcion} />

      <label>URL Imagen</label>
      <input type="text" bind:value={imagen} />

      <h3>Agregar Subcategorías</h3>

      <input type="text" placeholder="Nombre subcategoría" bind:value={subNombre} />
      <input type="number" placeholder="Precio" bind:value={subPrecio} />

      <label>Polizas que cubren:</label>
      <select multiple bind:value={subPolizas}>
        {#each opcionesPolizas as poliza}
          <option value={poliza}>{poliza}</option>
        {/each}
      </select>

      <button on:click={agregarSubcategoria}>Agregar Subcategoría</button>

      <h4>Subcategorías Agregadas:</h4>
      <ul>
        {#each subcategorias as sub, index}
          <li>
            <strong>{sub.nombre}</strong> - Q{sub.precio}
            <br />
            <em>Cubre: {sub.polizas_cubren.join(", ")}</em>
            <button on:click={() => eliminarSubcategoria(index)}>❌</button>
          </li>
        {/each}
      </ul>

      <div class="acciones">
        <button on:click={guardarServicio}>💾 Guardar</button>
        <button on:click={() => showModal = false}>❌ Cancelar</button>
      </div>
    </div>
  </div>
{/if}

<style>
  h2 {
    margin-bottom: 1rem;
  }

  .btn-crear {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    margin-bottom: 20px;
    border: none;
    cursor: pointer;
  }

  .servicios-container {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
  }

  .servicio-card {
    background-color: #fff;
    border-radius: 8px;
    padding: 15px;
    width: 300px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.2);
  }

  .servicio-card img {
    width: 100%;
    height: 150px;
    object-fit: cover;
    margin-bottom: 10px;
  }

  .acciones button {
    margin-right: 10px;
    margin-top: 10px;
  }

  .modal {
    position: fixed;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0,0,0,0.5);
    display: flex; justify-content: center; align-items: center;
  }

  .modal-content {
    background: white;
    padding: 30px;
    width: 500px;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
  }

  .modal-content input, select {
    margin-bottom: 10px;
    padding: 8px;
    border-radius: 4px;
    border: 1px solid #ccc;
  }

  select[multiple] {
    height: 80px;
  }

  .modal-content button {
    margin-top: 10px;
  }

  ul {
    list-style: none;
    padding-left: 0;
  }

  li {
    margin-bottom: 8px;
  }
</style>
