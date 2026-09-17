package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoRequest;
import com.Proyecto.Gestor_Contable.dtos.TipoMovimientoResponse;
import com.Proyecto.Gestor_Contable.service.TipoMovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-movimiento")
@CrossOrigin(origins = "*")
public class TipoMovimientoController {
    @Autowired
    private TipoMovimientoService tipoMovimientoService;

    @PostMapping
    public ResponseEntity<TipoMovimientoResponse> crear(@RequestBody TipoMovimientoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoMovimientoService.crearTipo(request));
    }
    @GetMapping
    public ResponseEntity<List<TipoMovimientoResponse>> listarTodo(){
        return ResponseEntity.ok(tipoMovimientoService.listarTodo());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TipoMovimientoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(tipoMovimientoService.buscarPorId(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<TipoMovimientoResponse> actualizar(@PathVariable String id,
                                                             @RequestBody TipoMovimientoRequest request) {
        return ResponseEntity.ok(tipoMovimientoService.actualizar(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        tipoMovimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}