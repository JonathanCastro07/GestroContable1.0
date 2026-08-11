
package com.Proyecto.Gestor_Contable.Controller;


import com.Proyecto.Gestor_Contable.Modelo.PagoPeriodico;
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
    public ResponseEntity<PagoPeriodico> crear(@RequestBody PagoPeriodico pago) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.crear(pago));
    }

    @GetMapping("/negocio/{idNegocio}")
    public ResponseEntity<List<PagoPeriodico>> listarPorNegocio(@PathVariable Long idNegocio) {
        return ResponseEntity.ok(pagoService.listarPorNegocio(idNegocio));
    }

    @GetMapping("/negocio/{idNegocio}/activos")
    public ResponseEntity<List<PagoPeriodico>> listarActivos(@PathVariable Long idNegocio) {
        return ResponseEntity.ok(pagoService.listarActivosPorNegocio(idNegocio));
    }

    @GetMapping("/negocio/{idNegocio}/proximos")
    public ResponseEntity<List<PagoPeriodico>> listarProximos(@PathVariable Long idNegocio) {
        return ResponseEntity.ok(pagoService.listarProximos(idNegocio));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoPeriodico> buscarPorId(@PathVariable Long id) {
        return pagoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoPeriodico> actualizar(@PathVariable Long id,
                                                    @RequestBody PagoPeriodico pago) {
        return ResponseEntity.ok(pagoService.actualizar(id, pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ejecutar")
    public ResponseEntity<Void> ejecutarPago(@PathVariable Long id) {
        pagoService.ejecutarPago(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<PagoPeriodico>> listarPorPeriodo(
            @PathVariable Long idNegocio,
            @RequestParam Integer mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(pagoService.listarPorPeriodo(idNegocio, mes, anio));
    }
}
