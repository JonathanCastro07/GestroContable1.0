package com.Proyecto.Gestor_Contable.dtos;

import java.time.LocalDateTime;

public record AnalisisFinancieroIAResponse(
        String id,
        String negocioid,
        String periodo,
        String textoGenerado,
        LocalDateTime fechaGeneracion,
        String tipoAlerta
) {}

