package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.OrigenRequest;
import com.Proyecto.Gestor_Contable.dtos.OrigenResponse;
import com.Proyecto.Gestor_Contable.service.OrigenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/origen")
@CrossOrigin(origins = "*")
public class OrigenController {
    @Autowired
    private OrigenService origenService;

    @PostMapping
    public ResponseEntity<OrigenResponse> crear(@RequestBody OrigenRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(origenService.crear(request));
    }
    @GetMapping
    public ResponseEntity<List<OrigenResponse>> listar(){
        return ResponseEntity.ok(origenService.listarTodo());
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrigenResponse> buscarPorId(@PathVariable String id){
        return ResponseEntity.ok(origenService.buscarPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<OrigenResponse> actualizar(@PathVariable String id, @RequestBody OrigenRequest request){
        return ResponseEntity.ok(origenService.actualizar(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        origenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
