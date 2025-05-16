package com.medtech.hospitales.dtos;

import java.time.LocalDate;
import java.util.List;

public class RecetaDTO {

    private String codigoReceta;
    private LocalDate fechaCita;
    private String nombrePaciente;
    private String nombreDoctor;
    private String numColegiado;
    private List<String> especialidades;
    private String anotaciones;
    private List<MedicamentoRecetadoDTO> medicamentos;
    private String tituloHospital;
    private List<String> telefonosDoctor;
    private String diagnostico;
    private String pasosSiguientes;

    private Long idRM;
    private Double total;

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
    

public Double getTotal() {
    return total;
}

public void setTotal(Double total) {
    this.total = total;
}


public Long getIdRM() {
    return idRM;
}

public void setIdRM(Long idRM) {
    this.idRM = idRM;
}


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
}
