package com.hospital.medtech.controllers;

import com.hospital.medtech.models.Usuario;
import com.hospital.medtech.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // REGISTRO
    @PostMapping("/registro")
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    // LOGIN PARA HOSPITALES
    @PostMapping("/loginh")
    public ResponseEntity<Usuario> loginHospital(@RequestParam String correo, @RequestParam String contrasena, HttpSession session) {
        Optional<Usuario> usuario = usuarioService.validarCredencialesHospital(correo, contrasena);

        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        session.setAttribute("usuario", usuario.get());
        return ResponseEntity.ok(usuario.get());
    }

    // OBTENER USUARIO ACTUAL
    @GetMapping("/sesion")
    public ResponseEntity<Usuario> obtenerUsuarioAutenticado(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usuario);
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }
}
