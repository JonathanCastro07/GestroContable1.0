package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.Service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
//nuevopost
//f













@RestController
@RequestMapping("/api/movimiento")
@CrossOrigin(origins = "*")
public class MovimientoController {
    @Autowired
    private MovimientoService movimientoService;

    @PostMapping("/registro")
    public ResponseEntity<MovimientoFinanciero> registrarMovimiento(@RequestBody MovimientoFinanciero movimiento){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrarMovimiento(movimiento));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoFinanciero> editarMovimiento(@PathVariable Long id,
                                                 @RequestBody MovimientoFinanciero movimiento){
        return ResponseEntity.ok(movimientoService.editarMovimiento(id, movimiento));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        movimientoService.eliminarMovimiento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/negocio/{id}")
    public ResponseEntity<List<MovimientoFinanciero>> listarNegocio(@PathVariable Long id){
        return ResponseEntity.ok(movimientoService.listarPorNegocio(id));
    }
    @GetMapping("/negocio/{id}/fechas")
    public ResponseEntity<List<MovimientoFinanciero>>listarNegocioYFecha(@PathVariable Long id,
                                                         @RequestParam LocalDate desde,
                                                         @RequestParam LocalDate hasta){
        return ResponseEntity.ok(movimientoService.listarPorNegocioYFecha(id, desde, hasta));
    }
    @GetMapping("/negocio/{idNegocio}/periodo")
    public ResponseEntity<List<MovimientoFinanciero>> listarPorPeriodo(
            @PathVariable Long idNegocio,
            @RequestParam String mes,
            @RequestParam Integer anio) {
        return ResponseEntity.ok(movimientoService.listarPorPeriodo(idNegocio, mes, anio));
    }

}
