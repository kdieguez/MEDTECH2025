import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import './css/recetamedica.css';
import './css/modalmedicamento.css';
import html2pdf from 'html2pdf.js';

export default function RecetaMedica() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [idRM, setIdRM] = useState(null);
  const [receta, setReceta] = useState(null);
  const [comentario, setComentario] = useState('');
  const [recetaFinalizada, setRecetaFinalizada] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [nuevoMedicamento, setNuevoMedicamento] = useState({ nombre: '', principioActivo: '', concentracion: '', presentacion: '', formaFarmaceutica: '', precio:''});
  const [medicamentosDisponibles, setMedicamentosDisponibles] = useState([]);
  const [medicamentoSeleccionado, setMedicamentoSeleccionado] = useState('');
  const [dosis, setDosis] = useState('');
  const [frecuencia, setFrecuencia] = useState('');
  const [duracion, setDuracion] = useState('');
  const [medicamentosRecetados, setMedicamentosRecetados] = useState([]);

  const volverAFormulario = () => navigate(`/formularioCita/${id}`);

  const crearRecetaSiNoExiste = async () => {
    try {
      const response = await axios.post(`http://localhost:7000/receta/${id}/crear`);
      return response.data.idRM;
    } catch (error) {
      if (error.response?.status === 200 && error.response?.data?.idRM) {
        return error.response.data.idRM;
      } else {
        console.error("Error al crear receta:", error);
        throw error;
      }
    }
  }; 

  const descargarPDF = () => {
    const elemento = document.querySelector(".receta-container");
    const boton = document.querySelector(".btn-descargar-pdf");
    if (boton) boton.style.display = 'none';
    elemento.classList.add("sin-borde");

    html2pdf().set({
      margin: 0.2,
      filename: receta.codigoReceta + ".pdf",
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 5 },
      jsPDF: { unit: 'in', format: 'letter', orientation: 'portrait' }
    }).from(elemento).save().then(() => {
      if (boton) boton.style.display = 'inline-block';
      elemento.classList.remove("sin-borde");
    });
  };

  useEffect(() => {
    const inicializar = async () => {
      try {
        const recetaId = await crearRecetaSiNoExiste();
        console.log("ID de la receta:", recetaId);
        setIdRM(recetaId);
        const recetaRes = await axios.get(`http://localhost:7000/receta/${id}`);
        setReceta(recetaRes.data);
        if (recetaRes.data.anotaciones) {
          setComentario(recetaRes.data.anotaciones);
          setRecetaFinalizada(true);
        }
        const meds = await axios.get('http://localhost:7000/medicamentos');
        setMedicamentosDisponibles(meds.data);
      } catch (e) {
        console.error("Error de inicialización:", e);
      }
    };
    inicializar();
  }, [id]);

  const abrirModal = () => setShowModal(true);
  const cerrarModal = () => {
    setShowModal(false);
    setNuevoMedicamento({ nombre: '', principioActivo: '', concentracion: '', presentacion: '', formaFarmaceutica: '' });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNuevoMedicamento(prev => ({ ...prev, [name]: value }));
  };

  const guardarMedicamento = () => {
    axios.post('http://localhost:7000/medicamentos', nuevoMedicamento)
      .then(() => {
        alert('Medicamento agregado');
        cerrarModal();
        axios.get('http://localhost:7000/medicamentos')
          .then(response => setMedicamentosDisponibles(response.data));
      })
      .catch(() => alert('Error al agregar medicamento'));
  };

  const agregarMedicamentoRecetado = () => {
    if (!medicamentoSeleccionado || !dosis || !frecuencia || !duracion) {
      alert('Completa todos los campos');
      return;
    }
    const nuevo = { idMedicamento: parseInt(medicamentoSeleccionado, 10), dosis, frecuencia, duracion };
    setMedicamentosRecetados([...medicamentosRecetados, nuevo]);
    setMedicamentoSeleccionado(''); setDosis(''); setFrecuencia(''); setDuracion('');
  };

  const guardarRecetaCompleta = async () => {
    if (medicamentosRecetados.length === 0 || !comentario.trim()) {
      alert("Completa medicamentos y comentarios");
      return;
    }
    try {
      await axios.post(`http://localhost:7000/receta/${idRM}/guardar-medicamentos`, medicamentosRecetados);
      await axios.post(`http://localhost:7000/receta/${idRM}/comentario?comentario=${encodeURIComponent(comentario)}`);
  
      const recetaRes = await axios.get(`http://localhost:7000/receta/${id}`);
      setReceta(recetaRes.data);
  
      setRecetaFinalizada(true);
      alert('Receta guardada');
    } catch {
      alert("Error al guardar receta");
    }
  };
  
  if (!receta || !idRM) return <div className="receta-container">Cargando...</div>;

  return (
    <div>
      <div className="receta-container">
        <div className="encabezado-receta">
          <h1>{receta.tituloHospital || "Hospital La Paz"}</h1>
          <h3>Receta Médica</h3>
        </div>

        <div className="info-receta">
          <p><strong>Fecha:</strong> {receta.fechaCita}</p>
          <p><strong>Código de Receta:</strong> {receta.codigoReceta}</p>
        </div>

        <div className="paciente-receta">
          <p><strong>Paciente:</strong> {receta.nombrePaciente}</p>
          <p><strong>Médico:</strong> {receta.nombreDoctor}</p>
          <p><strong>No. de Colegiado:</strong> {receta.numColegiado}</p>
          <p><strong>Especialidades:</strong> {receta.especialidades?.join(', ')}</p>
        </div>

        {!recetaFinalizada && (
          <>
            <button className="btn-agregar-medicamento" onClick={abrirModal}>Agregar nuevo medicamento</button>

            <div className="agregar-medicamento">
              <h3>Agregar Medicamento</h3>
              <select value={medicamentoSeleccionado} onChange={(e) => setMedicamentoSeleccionado(e.target.value)}>
                <option value="">Selecciona un medicamento</option>
                {medicamentosDisponibles.map(m => (
                  <option key={m.idMedicamento} value={m.idMedicamento}>{m.nombre}</option>
                ))}
              </select>
              <input type="text" placeholder="Dosis" value={dosis} onChange={(e) => setDosis(e.target.value)} />
              <input type="text" placeholder="Frecuencia" value={frecuencia} onChange={(e) => setFrecuencia(e.target.value)} />
              <input type="text" placeholder="Duración" value={duracion} onChange={(e) => setDuracion(e.target.value)} />
              <button onClick={agregarMedicamentoRecetado}>Agregar a Receta</button>
            </div>
          </>
        )}

        {showModal && (
          <div className="modal-overlay">
            <div className="modal-content">
              <h2>Nuevo Medicamento</h2>
              <input type="text" name="nombre" placeholder="Nombre" value={nuevoMedicamento.nombre} onChange={handleInputChange} />
              <input type="text" name="principioActivo" placeholder="Principio Activo" value={nuevoMedicamento.principioActivo} onChange={handleInputChange} />
              <input type="text" name="concentracion" placeholder="Concentración" value={nuevoMedicamento.concentracion} onChange={handleInputChange} />
              <input type="text" name="presentacion" placeholder="Presentación" value={nuevoMedicamento.presentacion} onChange={handleInputChange} />
              <input type="text" name="formaFarmaceutica" placeholder="Forma Farmacéutica" value={nuevoMedicamento.formaFarmaceutica} onChange={handleInputChange} />
              <input type="number" name="precio" placeholder="Precio" step="0.01" value={nuevoMedicamento.precio} onChange={handleInputChange}/>
              <div className="botones-modal">
                <button onClick={guardarMedicamento} className="btn-guardar">Guardar</button>
                <button onClick={cerrarModal} className="btn-cancelar">Cancelar</button>
              </div>
            </div>
          </div>
        )}

        <div className="tabla-medicamentos">
          <h4>Medicamentos Recetados:</h4>
          <table>
            <thead>
              <tr>
                <th>Medicamento</th>
                <th>Dosis</th>
                <th>Frecuencia</th>
                <th>Duración</th>
                <th>Precio</th>
              </tr>
            </thead>
            <tbody>
      {(recetaFinalizada ? receta.medicamentos : medicamentosRecetados).map((med, index) => {
        const info = medicamentosDisponibles.find(m => m.idMedicamento === med.idMedicamento);
        return (
          <tr key={index}>
            <td>{info?.nombre || "Desconocido"}</td>
            <td>{med.dosis}</td>
            <td>{med.frecuencia}</td>
            <td>{med.duracion}</td>
            <td>Q.{info?.precio?.toFixed(2) || "0.00"}</td>
          </tr>
        );
      })}
    </tbody>
          </table>
        </div>

        <div className="anotaciones-receta">
          <h4>{recetaFinalizada ? "Anotaciones del Doctor" : "Agregar Comentarios"}</h4>
          {recetaFinalizada
            ? <p>{comentario || receta.anotaciones}</p>
            : <textarea rows="4" value={comentario} onChange={(e) => setComentario(e.target.value)} placeholder="Anotaciones..." />}
        </div>

        {recetaFinalizada && (
  <div style={{ marginTop: '20px', textAlign: 'right', fontWeight: 'bold' }}>
    Total: Q.{receta.total?.toFixed(2)}
  </div>
)}

        {!recetaFinalizada && (
          <button className="btn-guardar-receta" onClick={guardarRecetaCompleta}>Guardar Receta</button>
        )}

        <div className="footer-receta">
          <p> 
          {(receta.tituloHospital || "Hospital") + (receta.telefonosDoctor?.length ? " | " + receta.telefonosDoctor.join(" | ") : "")}

          </p>  
        </div>
      </div>

      {recetaFinalizada && (
        <button className="btn-descargar-pdf" onClick={descargarPDF}>Descargar PDF</button>
      )}

      <button className="btn-volver-formulario" onClick={volverAFormulario}>Volver</button>
    </div>
  );
}

