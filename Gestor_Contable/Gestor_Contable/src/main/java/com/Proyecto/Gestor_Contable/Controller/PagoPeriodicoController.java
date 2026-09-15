package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.DTO.PagoPeriodicoRequest;
import com.Proyecto.Gestor_Contable.DTO.PagoPeriodicoResponse;
import com.Proyecto.Gestor_Contable.Service.PagoPeriodicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos-periodicos")
@CrossOrigin(origins = "*")
public class PagoPeriodicoController {

    @Autowired
    private PagoPeriodicoService pagoService;

    @PostMapping
    public ResponseEntity<PagoPeriodicoResponse> crear(@RequestBody PagoPeriodicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.crear(request));
    }

    @GetMapping("/negocio/{idNegocio}")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarPorNegocio(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarPorNegocio(idNegocio));
    }

    @GetMapping("/negocio/{idNegocio}/activos")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarActivos(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarActivosPorNegocio(idNegocio));
    }

    @GetMapping("/negocio/{idNegocio}/proximos")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarProximos(@PathVariable String idNegocio) {
        return ResponseEntity.ok(pagoService.listarProximos(idNegocio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoPeriodicoResponse> buscarPorId(@PathVariable String id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoPeriodicoResponse> actualizar(@PathVariable String id,
                                                            @RequestBody PagoPeriodicoRequest request) {
        return ResponseEntity.ok(pagoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ejecutar")
    public ResponseEntity<Void> ejecutarPago(@PathVariable String id) {
        pagoService.ejecutarPago(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<PagoPeriodicoResponse>> listarPorPeriodo(
            @PathVariable String idNegocio,
            @RequestParam Integer mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(pagoService.listarPorPeriodo(idNegocio, mes, anio));
    }
}