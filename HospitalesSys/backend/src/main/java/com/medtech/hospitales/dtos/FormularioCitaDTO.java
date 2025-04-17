package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public class FormularioCitaDTO {

    @JsonProperty("idCita")
    private Long idCita;

 
    private List<String> urlsResultadosExamenes;

    private String diagnostico;
    private String pasosSiguientes;


    private boolean crearRecetaMedica;
    private String codigoReceta;
    private LocalDate fechaReceta;
    private List<MedicamentoRecetadoDTO> medicamentos;

    // Getters y setters
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
