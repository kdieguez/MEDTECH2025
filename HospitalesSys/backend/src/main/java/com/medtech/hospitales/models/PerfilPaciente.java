package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa el perfil detallado de un paciente en el sistema hospitalario.
 * <p>
 * Incluye información como documento de identificación, número de afiliación al seguro,
 * código del seguro, carné, fotografía y la asociación con el usuario correspondiente.
 * </p>
 */
@Entity
@Table(name = "PERFIL_PACIENTES")
public class PerfilPaciente {

    /** Identificador único del perfil de paciente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    /** Documento oficial de identificación del paciente (ej. DPI, pasaporte). */
    @Column(name = "DOCUMENTOIDENTIFICACION")
    private String documentoIdentificacion;

    /** Número de afiliación del paciente a su seguro médico. */
    @Column(name = "NUMEROAFILIACION")
    private String numeroAfiliacion;

    /** Código de seguro asociado al paciente. */
    @Column(name = "CODSEGURO")
    private String codSeguro;

    /** Número de carné que identifica al paciente en su institución médica. */
    @Column(name = "CARNET")
    private String carnet;

    /** URL o referencia de la fotografía del paciente. */
    @Column(name = "FOTOGRAFIA")
    private String fotografia;

    /** Usuario del sistema asociado a este perfil de paciente. */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentoIdentificacion() {
        return documentoIdentificacion;
    }

    public void setDocumentoIdentificacion(String d) {
        this.documentoIdentificacion = d;
    }

    public String getNumeroAfiliacion() {
        return numeroAfiliacion;
    }

    public void setNumeroAfiliacion(String numeroAfiliacion) {
        this.numeroAfiliacion = numeroAfiliacion;
    }

    public String getCodSeguro() {
        return codSeguro;
    }

    public void setCodSeguro(String codSeguro) {
        this.codSeguro = codSeguro;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Método alternativo para establecer el código de afiliado (alias de {@link #setNumeroAfiliacion}).
     * 
     * @param codigo código de afiliado
     */
    public void setCodigoAfiliado(String codigo) {
        this.numeroAfiliacion = codigo;
    }

    /**
     * Método alternativo para establecer el número de carné (alias de {@link #setCarnet}).
     * 
     * @param carnet número de carné
     */
    public void setNumeroCarnet(String carnet) {
        this.carnet = carnet;
    }

    /**
     * Método de placeholder para establecer fecha de nacimiento.
     * <p>
     * Este método no tiene efecto ya que la propiedad no está definida.
     * </p>
     * 
     * @param fecha fecha de nacimiento (no utilizada)
     */
    public void setFechaNacimiento(LocalDate fecha) {
    }
}
