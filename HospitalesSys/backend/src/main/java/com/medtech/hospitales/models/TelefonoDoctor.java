package com.medtech.hospitales.models;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "TELEFONO_DOCTOR")
public class TelefonoDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TELEFONO")
    private Long id;

    @Column(name = "TELEFONO")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    @JsonBackReference
    private InfoDoctor infoDoctor;

    public TelefonoDoctor() {}

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }
    
    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }
    
}

