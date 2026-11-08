package com.Proyecto.Gestor_Contable.controller;


import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class CuentaController {

    private final NegocioRepository negocioRepository;
    private final PasswordEncoder passwordEncoder;

    public CuentaController(NegocioRepository negocioRepository, PasswordEncoder passwordEncoder) {
        this.negocioRepository = negocioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public record CorreoRequest(String nuevoCorreo) {}
    public record PasswordRequest(String passwordActual, String nuevaPassword) {}

    @PutMapping("/correo")
    public ResponseEntity<Void> cambiarCorreo(@RequestBody CorreoRequest request, Authentication authentication) {
        String actual = authentication.getName();
        String nuevo = request.nuevoCorreo() == null ? "" : request.nuevoCorreo().trim();

        if (nuevo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nuevo correo es obligatorio");
        }
        if (!nuevo.equalsIgnoreCase(actual) && negocioRepository.existsByUsuario_Correo(nuevo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese correo ya está registrado");
        }

        List<Negocio> negocios = negociosDe(actual);
        negocios.forEach(n -> n.getUsuario().setCorreo(nuevo));
        negocioRepository.saveAll(negocios);

        // El token actual está firmado con el correo anterior: el frontend pide iniciar sesión de nuevo
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Void> cambiarPassword(@RequestBody PasswordRequest request, Authentication authentication) {
        List<Negocio> negocios = negociosDe(authentication.getName());

        String hashActual = negocios.get(0).getUsuario().getPassword();
        if (request.passwordActual() == null
                || !passwordEncoder.matches(request.passwordActual(), hashActual)) {
            // 400 y no 401: el frontend usa 401 para "sesión vencida"
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual no es correcta");
        }
        if (request.nuevaPassword() == null || request.nuevaPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La nueva contraseña es obligatoria");
        }

        String nuevoHash = passwordEncoder.encode(request.nuevaPassword());
        negocios.forEach(n -> n.getUsuario().setPassword(nuevoHash));
        negocioRepository.saveAll(negocios);
        return ResponseEntity.noContent().build();
    }

    private List<Negocio> negociosDe(String correo) {
        List<Negocio> negocios = negocioRepository.findByUsuario_Correo(correo);
        if (negocios.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        return negocios;
    }
}
