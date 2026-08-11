package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.Service.TipoMovimientoService;
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
    public ResponseEntity<TipoMovimiento> crear(@RequestBody TipoMovimiento tipoMovimiento){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoMovimientoService.crearTipo(tipoMovimiento));
    }
    @GetMapping
    public ResponseEntity<List<TipoMovimiento>>listarTodo(){
            return ResponseEntity.ok(tipoMovimientoService.listarTodo());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TipoMovimiento> buscarPorId(@PathVariable Long id) {
        return tipoMovimientoService.BusrcarID(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<TipoMovimiento> actualizar(@PathVariable Long id,
                                                     @RequestBody TipoMovimiento tipoMovimiento) {
        return ResponseEntity.ok(tipoMovimientoService.actualizar(id, tipoMovimiento));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoMovimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
