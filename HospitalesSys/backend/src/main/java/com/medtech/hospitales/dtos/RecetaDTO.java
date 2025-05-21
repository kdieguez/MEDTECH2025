package com.medtech.hospitales.dtos;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) que representa una receta médica completa generada tras una cita.
 * <p>
 * Contiene información del paciente, doctor, diagnóstico, medicamentos recetados, anotaciones y más.
 * Es utilizada para mostrar o imprimir una receta final al paciente.
 * </p>
 */
public class RecetaDTO {

    /** Código único de la receta médica (ej. "HOSP-SEG-00123"). */
    private String codigoReceta;

    /** Fecha en que se realizó la cita médica. */
    private LocalDate fechaCita;

    /** Nombre completo del paciente. */
    private String nombrePaciente;

    /** Nombre completo del doctor que atendió la cita. */
    private String nombreDoctor;

    /** Número de colegiado profesional del doctor. */
    private String numColegiado;

    /** Lista de especialidades médicas del doctor. */
    private List<String> especialidades;

    /** Comentarios o notas escritas por el doctor. */
    private String anotaciones;

    /** Lista de medicamentos recetados en la receta. */
    private List<MedicamentoRecetadoDTO> medicamentos;

    /** Título o nombre del hospital que emite la receta. */
    private String tituloHospital;

    /** Lista de números telefónicos de contacto del doctor. */
    private List<String> telefonosDoctor;

    /** Diagnóstico registrado durante la cita médica. */
    private String diagnostico;

    /** Recomendaciones o pasos a seguir posteriores a la cita. */
    private String pasosSiguientes;

    /** Identificador de la receta médica en la base de datos. */
    private Long idRM;

    /** Total monetario calculado para la receta (si aplica). */
    private Double total;

    // --- Getters y Setters ---

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public LocalDate getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getNombreDoctor() {
        return nombreDoctor;
    }

    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public List<String> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }

    public String getAnotaciones() {
        return anotaciones;
    }

    public void setAnotaciones(String anotaciones) {
        this.anotaciones = anotaciones;
    }

    public List<MedicamentoRecetadoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoRecetadoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getTituloHospital() {
        return tituloHospital;
    }

    public void setTituloHospital(String tituloHospital) {
        this.tituloHospital = tituloHospital;
    }

    public List<String> getTelefonosDoctor() {
        return telefonosDoctor;
    }

    public void setTelefonosDoctor(List<String> telefonosDoctor) {
        this.telefonosDoctor = telefonosDoctor;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPasosSiguientes() {
        return pasosSiguientes;
    }

    public void setPasosSiguientes(String pasosSiguientes) {
        this.pasosSiguientes = pasosSiguientes;
    }

    public Long getIdRM() {
        return idRM;
    }

    public void setIdRM(Long idRM) {
        this.idRM = idRM;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
