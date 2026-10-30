package com.Proyecto.Gestor_Contable.dtos;

import java.time.LocalDateTime;

public record AnalisisFinancieroIAResponse(
        String id,
        String negocioId,
        PeriodoDto periodo,
        String textoGenerado,
        String tipoAlerta,
        LocalDateTime fechaGeneracion

) {}

