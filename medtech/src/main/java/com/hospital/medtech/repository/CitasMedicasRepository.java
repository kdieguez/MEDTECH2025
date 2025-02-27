package com.hospital.medtech.repository;

import com.hospital.medtech.models.CitasMedicas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitasMedicasRepository extends JpaRepository<CitasMedicas, Long> {
    List<CitasMedicas> findByFechaHora(LocalDateTime fechaHora);
}