package com.medtech.hospitales.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;


@Entity
@Table(name = "INFO_DOCTOR")
public class InfoDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INFO_DOCTOR")
    private Long id;

    @Column(name = "FOTOGRAFIA")
    private String fotografia; 

    @Column(name = "NUMCOLEGIADO")
    private String numColegiado;

    @OneToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @OneToMany(mappedBy = "infoDoctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TelefonoDoctor> telefonos = new ArrayList<>();

    @OneToMany(mappedBy = "infoDoctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EspecialidadDoctor> especialidades = new ArrayList<>();

    public InfoDoctor() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFotografia() {
        return fotografia;
    }
    
    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
    
    public String getNumColegiado() {
        return numColegiado;
    }
    
    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public List<TelefonoDoctor> getTelefonos() {
        return telefonos;
    }
    
    public void setTelefonos(List<TelefonoDoctor> telefonos) {
        this.telefonos = telefonos;
    }
    
    public List<EspecialidadDoctor> getEspecialidades() {
        return especialidades;
    }
    
    public void setEspecialidades(List<EspecialidadDoctor> especialidades) {
        this.especialidades = especialidades;
    }
}    

