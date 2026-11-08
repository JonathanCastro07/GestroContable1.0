package com.Proyecto.Gestor_Contable.controller;

import com.Proyecto.Gestor_Contable.dtos.GraficoMensualResponse;
import com.Proyecto.Gestor_Contable.service.lmpl.GraficoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grafico")
public class GraficoController {

    private final GraficoService graficoService;

    public GraficoController(GraficoService graficoService) {
        this.graficoService = graficoService;
    }

    @GetMapping("/negocio/{negocioId}/mensual")
    @PreAuthorize("@negocioSecurity.esDueno(#negocioId, authentication.name)")
    public ResponseEntity<GraficoMensualResponse> mensual(@PathVariable String negocioId,
                                                          @RequestParam(required = false) Integer anio) {
        return ResponseEntity.ok(graficoService.mensual(negocioId, anio));
    }
}

