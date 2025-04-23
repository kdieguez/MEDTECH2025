package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para representar el formulario que se llena
 * al concluir una cita médica, incluyendo diagnóstico, pasos a seguir, resultados de exámenes
 * y la generación de una receta médica si corresponde.
 */
public class FormularioCitaDTO {

    /**
     * Identificador de la cita médica asociada al formulario.
     */
    @JsonProperty("idCita")
    private Long idCita;

    /**
     * Lista de URLs donde se almacenan los resultados de los exámenes médicos.
     */
    private List<String> urlsResultadosExamenes;

    /**
     * Diagnóstico realizado durante la cita médica.
     */
    private String diagnostico;

    /**
     * Recomendaciones o pasos a seguir posteriores a la cita.
     */
    private String pasosSiguientes;

    /**
     * Indica si se debe crear una receta médica asociada al formulario.
     */
    private boolean crearRecetaMedica;

    /**
     * Código único asociado a la receta médica, si se genera.
     */
    private String codigoReceta;

    /**
     * Fecha de creación de la receta médica.
     */
    private LocalDate fechaReceta;

    /**
     * Lista de medicamentos recetados en la receta médica.
     */
    private List<MedicamentoRecetadoDTO> medicamentos;

    /**
     * Obtiene el ID de la cita médica.
     *
     * @return ID de la cita
     */
    public Long getIdCita() {
        return idCita;
    }

    /**
     * Establece el ID de la cita médica.
     *
     * @param idCita ID de la cita
     */
    public void setIdCita(Long idCita) {
        this.idCita = idCita;
    }

    /**
     * Obtiene la lista de URLs de resultados de exámenes.
     *
     * @return lista de URLs de resultados
     */
    public List<String> getUrlsResultadosExamenes() {
        return urlsResultadosExamenes;
    }

    /**
     * Establece la lista de URLs de resultados de exámenes.
     *
     * @param urlsResultadosExamenes lista de URLs de resultados
     */
    public void setUrlsResultadosExamenes(List<String> urlsResultadosExamenes) {
        this.urlsResultadosExamenes = urlsResultadosExamenes;
    }

    /**
     * Obtiene el diagnóstico de la cita.
     *
     * @return diagnóstico médico
     */
    public String getDiagnostico() {
        return diagnostico;
    }

    /**
     * Establece el diagnóstico de la cita.
     *
     * @param diagnostico diagnóstico médico
     */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /**
     * Obtiene los pasos a seguir tras la cita.
     *
     * @return pasos siguientes
     */
    public String getPasosSiguientes() {
        return pasosSiguientes;
    }

    /**
     * Establece los pasos a seguir tras la cita.
     *
     * @param pasosSiguientes pasos siguientes
     */
    public void setPasosSiguientes(String pasosSiguientes) {
        this.pasosSiguientes = pasosSiguientes;
    }

    /**
     * Verifica si se debe crear una receta médica.
     *
     * @return true si se debe crear la receta, false en caso contrario
     */
    public boolean isCrearRecetaMedica() {
        return crearRecetaMedica;
    }

    /**
     * Establece si se debe crear una receta médica.
     *
     * @param crearRecetaMedica true para crear la receta, false para omitir
     */
    public void setCrearRecetaMedica(boolean crearRecetaMedica) {
        this.crearRecetaMedica = crearRecetaMedica;
    }

    /**
     * Obtiene el código de la receta médica.
     *
     * @return código de la receta
     */
    public String getCodigoReceta() {
        return codigoReceta;
    }

    /**
     * Establece el código de la receta médica.
     *
     * @param codigoReceta código de la receta
     */
    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    /**
     * Obtiene la fecha de la receta médica.
     *
     * @return fecha de la receta
     */
    public LocalDate getFechaReceta() {
        return fechaReceta;
    }

    /**
     * Establece la fecha de la receta médica.
     *
     * @param fechaReceta fecha de la receta
     */
    public void setFechaReceta(LocalDate fechaReceta) {
        this.fechaReceta = fechaReceta;
    }

    /**
     * Obtiene la lista de medicamentos recetados.
     *
     * @return lista de medicamentos
     */
    public List<MedicamentoRecetadoDTO> getMedicamentos() {
        return medicamentos;
    }

    /**
     * Establece la lista de medicamentos recetados.
     *
     * @param medicamentos lista de medicamentos
     */
    public void setMedicamentos(List<MedicamentoRecetadoDTO> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
