package com.hospital.medtech.service;

import com.hospital.medtech.models.Usuario;
import com.hospital.medtech.models.Rol;
import com.hospital.medtech.repository.UsuarioRepository;
import com.hospital.medtech.repository.RolRepository;
import com.hospital.medtech.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    // REGISTRAR USUARIO
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        usuario.setContrasena(HashUtil.hashSHA256(usuario.getContrasena()));

        Optional<Rol> rolPaciente = rolRepository.findByNombre("Paciente");
        usuario.setRol(rolPaciente.orElseThrow(() -> new RuntimeException("Rol 'Paciente' no encontrado")));

        return usuarioRepository.save(usuario);
    }

    // VALIDAR LOGIN PARA HOSPITALES
    public Optional<Usuario> validarCredencialesHospital(String correo, String contrasena) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        if (usuario.isPresent()) {
            String hashedPassword = HashUtil.hashSHA256(contrasena);
            if (usuario.get().getContrasena().equals(hashedPassword)) {
                return usuario;
            }
        }
        return Optional.empty();
    }
}
