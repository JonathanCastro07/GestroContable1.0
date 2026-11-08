package com.Proyecto.Gestor_Contable.dtos;

import java.util.List;

public record GraficoMensualResponse(
        String negocioId,
        int anio,
        List<Integer> aniosDisponibles,
        List<MesGrafico> meses,
        TotalesGrafico totales
) {
    public record MesGrafico(int mes, String etiqueta, double ingresos, double egresos,
                             double gastos, double utilidad) {}

    public record TotalesGrafico(double ingresos, double egresos, double gastos, double utilidad) {}
}
