package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "PERFIL_PACIENTES")
public class PerfilPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    @Column(name = "DOCUMENTOIDENTIFICACION")
    private String documentoIdentificacion;

    @Column(name = "NUMEROAFILIACION")
    private String numeroAfiliacion;

    @Column(name = "CODSEGURO")
    private String codSeguro;

    @Column(name = "CARNET")
    private String carnet;

    @Column(name = "FOTOGRAFIA")
    private String fotografia;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID_USUARIO")
    private Usuario usuario;

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
