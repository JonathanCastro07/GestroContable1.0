package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.dtos.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.service.PagoPeriodicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos-periodicos")
@CrossOrigin(origins = "*")
public class PagoPeriodicoController {

    @Autowired
    private PagoPeriodicoService pagoService;

    @PreAuthorize("@negocioSecurity.esDueno(#request.negocioId(), authentication.name)")
    @PostMapping
    public ResponseEntity<PagoPeriodicoResponse> crear(@RequestBody PagoPeriodicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.crear(request));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#idNegocio, authentication.name)")
    @GetMapping("/negocio/{idNegocio}")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarPorNegocio(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarPorNegocio(idNegocio));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#idNegocio, authentication.name)")
    @GetMapping("/negocio/{idNegocio}/activos")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarActivos(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarActivosPorNegocio(idNegocio));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#idNegocio, authentication.name)")
    @GetMapping("/negocio/{idNegocio}/proximos")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarProximos(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarProximos(idNegocio));
    }

    @PreAuthorize("@negocioSecurity.esDuenoDePago(#id, authentication.name)")
    @GetMapping("/{id}")
    public ResponseEntity<PagoPeriodicoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PreAuthorize("@negocioSecurity.esDuenoDePago(#id, authentication.name) and @negocioSecurity.esDueno(#request.negocioId(), authentication.name)")
    @PutMapping("/{id}")
    public ResponseEntity<PagoPeriodicoResponse> actualizar(@PathVariable String id,
                                                            @RequestBody PagoPeriodicoRequest request) {
        return ResponseEntity.ok(pagoService.actualizar(id, request));
    }

    @PreAuthorize("@negocioSecurity.esDuenoDePago(#id, authentication.name)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@negocioSecurity.esDuenoDePago(#id, authentication.name)")
    @PostMapping("/{id}/ejecutar")
    public ResponseEntity<Void> ejecutarPago(@PathVariable String id) {
        pagoService.ejecutarPago(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@negocioSecurity.esDueno(#idNegocio, authentication.name)")
    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarPorPeriodo(
            @PathVariable String idNegocio,
            @RequestParam Integer mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(pagoService.listarPorPeriodo(idNegocio, mes, anio));
    }
}