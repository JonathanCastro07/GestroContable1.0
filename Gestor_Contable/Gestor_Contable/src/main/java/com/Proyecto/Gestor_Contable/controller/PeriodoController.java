package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.PeriodoRequest;
import com.Proyecto.Gestor_Contable.dtos.PeriodoResponse;
import com.Proyecto.Gestor_Contable.service.PeriodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodo")
@CrossOrigin(origins = "*")
public class PeriodoController {
    @Autowired
    private PeriodoService periodoService;

    @PostMapping
    public ResponseEntity<PeriodoResponse> crear(@RequestBody PeriodoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(periodoService.crear(request));
    }
    @GetMapping
    public ResponseEntity<List<PeriodoResponse>> listarTodo(){
        return ResponseEntity.ok(periodoService.listarTodo());
    }
    @GetMapping("/{id}")
    public ResponseEntity<PeriodoResponse> buscarPorId(@PathVariable String id){
        return ResponseEntity.ok(periodoService.buscarPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<PeriodoResponse> actualizar(@PathVariable String id, @RequestBody PeriodoRequest request){
        return ResponseEntity.ok(periodoService.actualizar(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        periodoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

