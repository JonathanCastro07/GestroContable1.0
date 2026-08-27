package com.Proyecto.Gestor_Contable.DTO;

import java.time.LocalDate;

public record MovimientoFinancieroRequest(
        double monto,
        LocalDate fecha,
        String descricion,
        String negocioId,
        String tipoId,
        String origenId,
        String periodoId
) {}
