package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.Service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movimiento")
@CrossOrigin(origins = "*")
public class MovimientoController {

    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/registro")
    public ResponseEntity<MovimientoFinancieroResponse> registrarMovimiento(@RequestBody MovimientoFinancieroRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarMovimiento(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoFinancieroResponse> editarMovimiento(@PathVariable String id,
                                                                         @RequestBody MovimientoFinancieroRequest request){
        return ResponseEntity.ok(movimientoService.editarMovimiento(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarNegocio(@PathVariable String id){
        return ResponseEntity.ok(movimientoService.listarPorNegocio(id));
    }

    @GetMapping("/negocio/{id}/fechas")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarNegocioYFecha(@PathVariable String id,
                                                                                  @RequestParam LocalDate desde,
                                                                                  @RequestParam LocalDate hasta){
        return ResponseEntity.ok(movimientoService.listarPorNegocioYFecha(id, desde, hasta));
    }

    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarPorPeriodo(
            @PathVariable String idNegocio,
            @RequestParam String mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(movimientoService.listarPorPeriodo(idNegocio, mes, anio));
    }

    @GetMapping("/resumen")
    public ResponseEntity<MovimientoResumenResponse> obtenerResumen(
            @RequestParam String negocioId,
            @RequestParam String periodo) {
        return ResponseEntity.ok(movimientoService.obtenerResumen(negocioId, periodo));
    }
}