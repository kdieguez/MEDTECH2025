package com.hospital.medtech.service;

import com.hospital.medtech.models.Doctor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DoctorService {

    private final List<Doctor> doctores = Arrays.asList(
            new Doctor(1, "Dr. Juan Pérez", "Cardiólogo", "Universidad X", "123456", "/img/doctor1.jpg",
                    Arrays.asList("/img/titulo1.jpg", "/img/titulo2.jpg"), "555-1234"),
            new Doctor(2, "Dra. María López", "Dermatóloga", "Universidad Y", "654321", "/img/doctor2.jpg",
                    Arrays.asList("/img/titulo3.jpg", "/img/titulo4.jpg"), "555-5678")
    );

    public List<Doctor> obtenerDoctores() {
        return doctores;
    }

    public Doctor obtenerDoctorPorId(int id) {
        return doctores.stream()
                .filter(d -> d.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
