package com.Proyecto.Gestor_Contable.Controller;

import com.Proyecto.Gestor_Contable.Modelo.Negocio;
import com.Proyecto.Gestor_Contable.Service.NegocioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/negocio")
@CrossOrigin(origins = "*")
public class NegocioController { //Anderson
    @Autowired
    private NegocioServicio negocioServicio;
    @PostMapping("/crear")
    public ResponseEntity<Negocio> crear(@RequestBody Negocio negocio){
        return ResponseEntity.status(HttpStatus.CREATED).body(negocioServicio.crear(negocio));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<Negocio>> listarPorUsuario(@PathVariable("id") Long idUsuario){
        return ResponseEntity.ok(negocioServicio.listarPorUsuario(idUsuario));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Negocio> actualizar(@PathVariable Long id, @RequestBody Negocio negocio){
        return ResponseEntity.ok(negocioServicio.actualizar(id, negocio));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        negocioServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/utilidad")
    public ResponseEntity<Double> calcularUtilidad(@PathVariable("id") Long idNegocio){
        return ResponseEntity.ok(negocioServicio.calcularUtilidades(idNegocio));
    }
    @GetMapping("/{id}/financiero")
    public ResponseEntity<Object> verResumen(@PathVariable("id") Long idNegocio){
        return ResponseEntity.ok(negocioServicio.verResumenFinanciero(idNegocio));
    }
}
