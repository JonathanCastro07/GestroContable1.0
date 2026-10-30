package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("@negocioSecurity.esDueno(#request.negocioId(), authentication.name)")
    @PostMapping("/registro")
    public ResponseEntity<MovimientoFinancieroResponse> registrarMovimiento(@RequestBody MovimientoFinancieroRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarMovimiento(request));
    }

    @PreAuthorize("@negocioSecurity.esDuenoDeMovimiento(#id, authentication.name) and @negocioSecurity.esDueno(#request.negocioId(), authentication.name)")
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoFinancieroResponse> editarMovimiento(@PathVariable String id,
                                                                         @RequestBody MovimientoFinancieroRequest request){
        return ResponseEntity.ok(movimientoService.editarMovimiento(id, request));
    }

    @PreAuthorize("@negocioSecurity.esDuenoDeMovimiento(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@negocioSecurity.esDueno(#id, authentication.name)")
    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarNegocio(@PathVariable String id){
        return ResponseEntity.ok(movimientoService.listarPorNegocio(id));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#id, authentication.name)")
    @GetMapping("/negocio/{id}/fechas")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarNegocioYFecha(@PathVariable String id,
                                                                                  @RequestParam LocalDate desde,
                                                                                  @RequestParam LocalDate hasta){
        return ResponseEntity.ok(movimientoService.listarPorNegocioYFecha(id, desde, hasta));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#idNegocio, authentication.name)")
    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<MovimientoFinancieroResponse>> listarPorPeriodo(
            @PathVariable String idNegocio,
            @RequestParam String mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(movimientoService.listarPorPeriodo(idNegocio, mes, anio));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#negocioId, authentication.name)")
    @GetMapping("/resumen")
    public ResponseEntity<MovimientoResumenResponse> obtenerResumen(
            @RequestParam String negocioId,
            @RequestParam String periodo) {
        return ResponseEntity.ok(movimientoService.obtenerResumen(negocioId, periodo));
    }
}