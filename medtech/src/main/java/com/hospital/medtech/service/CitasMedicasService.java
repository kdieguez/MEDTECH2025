package com.hospital.medtech.service;

import com.hospital.medtech.models.CitasMedicas;
import com.hospital.medtech.repository.CitasMedicasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CitasMedicasService {

    @Autowired
    private CitasMedicasRepository repository;

    public List<CitasMedicas> obtenerTodasLasCitas() {
        return repository.findAll();
    }

    public Optional<CitasMedicas> obtenerCitaPorId(Long id) {
        return repository.findById(id);
    }

    public List<CitasMedicas> obtenerCitasPorFecha(LocalDateTime fechaHora) {
        return repository.findByFechaHora(fechaHora);
    }

    public CitasMedicas agendarCita(CitasMedicas cita) {
        return repository.save(cita);
    }

    public void cancelarCita(Long id) {
        repository.deleteById(id);
    }
}

