package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.*;
import com.Proyecto.Gestor_Contable.security.JwtUtil;
import com.Proyecto.Gestor_Contable.security.TokenBlacklistService;
import com.Proyecto.Gestor_Contable.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/registro")
    public ResponseEntity<RegistroResponse> registrarse(@RequestBody RegistroRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.registrarse(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciaSesion(@RequestBody LoginRequest request){
        return ResponseEntity.ok(usuarioService.iniciaSesion(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> cerrarSesion(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        long segundosRestantes = jwtUtil.obtenerSegundosRestantes(token);
        tokenBlacklistService.agregarABlacklist(token, segundosRestantes);
        return ResponseEntity.noContent().build();
    }
}