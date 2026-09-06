package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.Origen;
import com.Proyecto.Gestor_Contable.Service.OrigenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/origen")
@CrossOrigin(origins = "*")
public class OrigenController {
    @Autowired
    private OrigenService origenService;

    @PostMapping
    public ResponseEntity<Origen> Crear(@RequestBody Origen origen){
        return  ResponseEntity.status(HttpStatus.CREATED).body(origenService.crear(origen));
    }
    @GetMapping
    public ResponseEntity<List<Origen>> listrar(){
        return ResponseEntity.ok(origenService.ListarTodo());
    }
    @GetMapping("/{id}")
    public  ResponseEntity<Origen> buscarPorId(@PathVariable Long id){
        return origenService.busrcarPorId(id)
                .map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }
    @PutMapping("/{id}")
    public ResponseEntity<Origen> actualizar(@PathVariable Long id, @RequestBody Origen origen){
        return ResponseEntity.ok(origenService.actualizar(id, origen));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        origenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
