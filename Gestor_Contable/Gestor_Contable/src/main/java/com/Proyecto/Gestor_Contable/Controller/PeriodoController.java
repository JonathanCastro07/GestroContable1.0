package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import com.Proyecto.Gestor_Contable.Service.PeriodoService;
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
    public ResponseEntity<Periodo> crear(@RequestBody Periodo periodo){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(periodoService.crear(periodo));
    }
    @GetMapping
    public ResponseEntity<List<Periodo>> listartodo(){
        return ResponseEntity.ok(periodoService.listarTodo());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Periodo>buscarPorid(@PathVariable Long id){
        return periodoService.BuscarPorId(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Periodo> actualizar(@PathVariable Long id, @RequestBody Periodo periodo){
        return ResponseEntity.ok(periodoService.actualizar(id, periodo));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        periodoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

