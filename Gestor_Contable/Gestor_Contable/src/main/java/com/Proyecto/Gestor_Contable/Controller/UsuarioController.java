package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.DTO.LoginRequest;
import com.Proyecto.Gestor_Contable.DTO.LoginResponse;
import com.Proyecto.Gestor_Contable.DTO.RegistroRequest;
import com.Proyecto.Gestor_Contable.DTO.UsuarioResponse;
import com.Proyecto.Gestor_Contable.Service.UsuarioService;
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

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrarse(@RequestBody RegistroRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.registrarse(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> iniciaSesion(@RequestBody LoginRequest request){
        return ResponseEntity.ok(usuarioService.iniciaSesion(request));
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