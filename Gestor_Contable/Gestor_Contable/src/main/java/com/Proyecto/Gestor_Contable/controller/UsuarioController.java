package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.LoginRequest;
import com.Proyecto.Gestor_Contable.dtos.LoginResponse;
import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.UsuarioResponse;
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
    private com.Proyecto.Gestor_Contable.security.JwtUtil jwtUtil;
    @Autowired
    private com.Proyecto.Gestor_Contable.security.TokenBlacklistService tokenBlacklistService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrarse(@RequestBody RegistroRequest request){
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

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar(){
        return ResponseEntity.ok(usuarioService.listarTodo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable String id){
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable String id, @RequestBody RegistroRequest request){
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}