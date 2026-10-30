package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import com.Proyecto.Gestor_Contable.service.AnalisisFinancieroIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analisis")
@CrossOrigin(origins = "*")
public class AnalisisFinancieroIAController {

    @Autowired
    private AnalisisFinancieroIAService analisisService;

    @PreAuthorize("@negocioSecurity.esDueno(#negocioId, authentication.name)")
    @PostMapping("/negocio/{negocioId}")
    public ResponseEntity<AnalisisFinancieroIAResponse> generar(
            @PathVariable String negocioId,
            @RequestParam(defaultValue = "mensual") String periodo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(analisisService.generarAnalisis(negocioId, periodo));
    }

    @PreAuthorize("@negocioSecurity.esDueno(#negocioId, authentication.name)")
    @GetMapping("/negocio/{negocioId}")
    public ResponseEntity<List<AnalisisFinancieroIAResponse>> listar(@PathVariable String negocioId) {
        return ResponseEntity.ok(analisisService.listarPorNegocio(negocioId));
    }
}
