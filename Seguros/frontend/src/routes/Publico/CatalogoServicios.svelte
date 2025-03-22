<script>
  import { onMount } from 'svelte';
  import axios from 'axios';
  import Swal from 'sweetalert2';
  import { userRol } from '../../store.js'; 

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
    let currentRol;

  $: userRol.subscribe(value => {
    currentRol = value;
  });

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

    const data = { nombre, descripcion, imagen, subcategorias };

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

  function togglePoliza(poliza, isChecked) {
    if (isChecked) {
      if (!subPolizas.includes(poliza)) {
        subPolizas = [...subPolizas, poliza];
      }
    } else {
      subPolizas = subPolizas.filter(p => p !== poliza);
    }
  }
</script>

<h2 class="titulo-catalogo">Catálogo de Servicios</h2>
{#if currentRol === 'empleado' || currentRol === 'admin'}
<button on:click={abrirModalCrear} class="btn-flotante">+</button>
{/if}
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
            <strong>{sub.nombre}</strong> - Q{sub.precio}<br />
            <em>Cubre: {sub.polizas_cubren.join(", ")}</em>
          </li>
        {/each}
      </ul>

      <div class="acciones">
      {#if currentRol === 'empleado' || currentRol === 'admin'}
        <button on:click={() => abrirModalEditar(servicio)}>Editar</button>
        <button on:click={() => eliminarServicio(servicio._id)}>Eliminar</button>
        {/if}
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

      <h4>Polizas que cubren:</h4>
      <div class="checkbox-container">
        {#each opcionesPolizas as poliza}
          <label>
            <input
              type="checkbox"
              checked={subPolizas.includes(poliza)}
              on:change={(e) => togglePoliza(poliza, e.target.checked)}
            />
            {poliza}
          </label>
        {/each}
      </div>

      <button on:click={agregarSubcategoria} class="btn-agregar-sub">Agregar Subcategoría</button>

      <h4>Subcategorías Agregadas:</h4>
      <ul>
        {#each subcategorias as sub, index}
          <li>
            <strong>{sub.nombre}</strong> - Q{sub.precio}<br />
            <em>Cubre: {sub.polizas_cubren.join(", ")}</em>
            <button on:click={() => eliminarSubcategoria(index)}>Eliminar</button>
          </li>
        {/each}
      </ul>

      <div class="acciones">
        <button on:click={guardarServicio}>Guardar</button>
        <button on:click={() => showModal = false}>Cancelar</button>
      </div>
    </div>
  </div>
{/if}

<style>
.titulo-catalogo {
  position: absolute; 
  top: 100px;    
  left: 50%;   
  transform: translateX(-50%);
  font-size: 2.8rem;
  font-weight: bold;
  color: #333;
  z-index: 999;
}

  .btn-flotante {
    position: fixed;
    top: 120px;
    right: 50px;
    background-color: #4CAF50;
    color: white;
    font-size: 24px;
    padding: 12px 18px;
    border-radius: 50%;
    border: none;
    cursor: pointer;
    box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    z-index: 1000;
    transition: transform 0.3s ease;
  }

  .btn-flotante:hover {
    transform: scale(1.1);
  }

 .servicios-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 30px;
  margin-top: 150px;
  margin-bottom: 100px;
}

  .servicio-card {
    background-color: #fff;
    border-radius: 10px;
    padding: 20px;
    width: 420px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.2);
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .servicio-card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
    border-radius: 8px;
    margin-bottom: 15px;
  }

  .acciones button {
    margin: 8px;
    padding: 8px 16px;
    border: none;
    background-color: #f1f1f1;
    cursor: pointer;
    transition: background 0.3s;
  }

  .acciones button:hover {
    background-color: #ddd;
  }

  /* Modal */
  .modal {
    position: fixed;
    top: 0; left: 0; right: 0; bottom: 0;
    background: rgba(0,0,0,0.5);
    display: flex; justify-content: center; align-items: center;
    z-index: 2000;
  }

  .modal-content {
    background: white;
    padding: 30px;
    width: 500px;
    max-height: 90vh;
    overflow-y: auto;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
  }

  .modal-content input {
    margin-bottom: 15px;
    padding: 10px;
    border-radius: 4px;
    border: 1px solid #ccc;
  }

  .checkbox-container {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
  }

  .checkbox-container label {
    margin-bottom: 8px;
  }

  .btn-agregar-sub {
    background-color: #4CAF50;
    color: white;
    border: none;
    padding: 8px 16px;
    cursor: pointer;
    margin-bottom: 10px;
  }

  .btn-agregar-sub:hover {
    background-color: #45a049;
  }

  ul {
    list-style: none;
    padding-left: 0;
  }

  li {
    margin-bottom: 8px;
  }

  @media (max-width: 600px) {
    .servicio-card {
      width: 90%;
    }

    .modal-content {
      width: 90%;
    }

    .btn-flotante {
      top: 80px;
      right: 20px;
    }
  }
</style>
