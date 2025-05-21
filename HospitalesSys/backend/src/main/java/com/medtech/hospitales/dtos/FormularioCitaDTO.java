package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para representar el formulario que se llena
 * al concluir una cita médica.
 * <p>
 * Incluye diagnóstico, pasos a seguir, resultados de exámenes y, si aplica,
 * los datos necesarios para la generación de una receta médica.
 * </p>
 */
public class FormularioCitaDTO {

    /** Identificador de la cita médica asociada al formulario. */
    @JsonProperty("idCita")
    private Long idCita;

    /** Lista de URLs donde se almacenan los resultados de los exámenes médicos. */
    private List<String> urlsResultadosExamenes;

    /** Diagnóstico realizado por el doctor durante la cita. */
    private String diagnostico;

    /** Recomendaciones o instrucciones médicas para el paciente luego de la cita. */
    private String pasosSiguientes;

    /** Indica si se debe generar una receta médica como parte del formulario. */
    private boolean crearRecetaMedica;

    /** Código único generado para la receta médica. */
    private String codigoReceta;

    /** Fecha en que se genera la receta médica. */
    private LocalDate fechaReceta;

    /** Lista de medicamentos recetados como parte de la receta médica. */
    private List<MedicamentoRecetadoDTO> medicamentos;

    public Long getIdCita() {
        return idCita;
    }

    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    public List<String> getUrlsResultadosExamenes() {
        return urlsResultadosExamenes;
    }

    public void setUrlsResultadosExamenes(List<String> urlsResultadosExamenes) {
        this.urlsResultadosExamenes = urlsResultadosExamenes;
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

    public boolean isCrearRecetaMedica() {
        return crearRecetaMedica;
    }

    public void setCrearRecetaMedica(boolean crearRecetaMedica) {
        this.crearRecetaMedica = crearRecetaMedica;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public LocalDate getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(LocalDate fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    public List<MedicamentoRecetadoDTO> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<MedicamentoRecetadoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
