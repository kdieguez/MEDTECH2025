package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa el perfil de un paciente en el sistema hospitalario.
 */
@Entity
@Table(name = "PERFIL_PACIENTES")
public class PerfilPaciente {

    /**
     * Identificador único del perfil de paciente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    /**
     * Documento de identificación del paciente (por ejemplo, DPI o pasaporte).
     */
    @Column(name = "DOCUMENTOIDENTIFICACION")
    private String documentoIdentificacion;

    /**
     * Número de afiliación del paciente al seguro médico.
     */
    @Column(name = "NUMEROAFILIACION")
    private String numeroAfiliacion;

    /**
     * Código de seguro asociado al paciente.
     */
    @Column(name = "CODSEGURO")
    private String codSeguro;

    /**
     * Número de carné del paciente.
     */
    @Column(name = "CARNET")
    private String carnet;

    /**
     * Fotografía del paciente.
     */
    @Column(name = "FOTOGRAFIA")
    private String fotografia;

    /**
     * Usuario asociado al perfil del paciente.
     */
    @OneToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocumentoIdentificacion() { return documentoIdentificacion; }
    public void setDocumentoIdentificacion(String d) { this.documentoIdentificacion = d; }

    public String getNumeroAfiliacion() { return numeroAfiliacion; }
    public void setNumeroAfiliacion(String numeroAfiliacion) { this.numeroAfiliacion = numeroAfiliacion; }

    public String getCodSeguro() { return codSeguro; }
    public void setCodSeguro(String codSeguro) { this.codSeguro = codSeguro; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getFotografia() { return fotografia; }
    public void setFotografia(String fotografia) { this.fotografia = fotografia; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
