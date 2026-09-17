package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;
import com.Proyecto.Gestor_Contable.service.NegocioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocio")
@CrossOrigin(origins = "*")
public class NegocioController {

    @Autowired
    private NegocioServicio negocioServicio;

    @PostMapping("/crear")
    public ResponseEntity<NegocioResponse> crear(@RequestBody NegocioRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(negocioServicio.crear(request));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<NegocioResponse>> listarPorUsuario(@PathVariable("id") String idUsuario){
        return ResponseEntity.ok(negocioServicio.listarPorUsuario(idUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NegocioResponse> actualizar(@PathVariable String id, @RequestBody NegocioRequest request){
        return ResponseEntity.ok(negocioServicio.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        negocioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/utilidad")
    public ResponseEntity<Double> calcularUtilidad(@PathVariable("id") String idNegocio){
        return ResponseEntity.ok(negocioServicio.calcularUtilidades(idNegocio));
    }

    @GetMapping("/{id}/financiero")
    public ResponseEntity<Object> verResumen(@PathVariable("id") String idNegocio){
        return ResponseEntity.ok(negocioServicio.verResumenFinanciero(idNegocio));
    }
}