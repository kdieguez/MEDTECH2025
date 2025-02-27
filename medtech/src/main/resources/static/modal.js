function mostrarFormulario() {
    document.getElementById("modalAgregar").style.display = "block";
}

function cerrarFormulario() {
    document.getElementById("modalAgregar").style.display = "none";
}

function agregarDoctor() {
    let data = {
        nombre: document.getElementById("nombre").value,
        apellido: document.getElementById("apellido").value,
        username: document.getElementById("username").value,
        correo: document.getElementById("correo").value,
        contraseña: document.getElementById("contraseña").value
    };

    fetch("/doctores/agregar", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error en la respuesta del servidor");
        }
        return response.json();
    })
    .then(data => {
        console.log("Doctor agregado:", data);
        alert("Doctor agregado correctamente.");
        location.reload();
    })
    .catch(error => {
        console.error("Error al agregar doctor:", error);
    });
}
