package com.Proyecto.Gestor_Contable.DTO;

import java.time.LocalDate;

public record MovimientoResumenResponse(
        String periodo,
        LocalDate desde,
        LocalDate hasta,
        double totalIngresos,
        double totalEgresos,
        double totalGanancias
) {}