package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import com.Proyecto.Gestor_Contable.dtos.PeriodoDto;
import com.Proyecto.Gestor_Contable.modelo.AnalisisFinancieroIA;

public class AnalisisFinancieroIAMapper {

    public static AnalisisFinancieroIAResponse toResponse(AnalisisFinancieroIA analisis) {
        PeriodoDto periodo = analisis.getPeriodo() == null ? null : new PeriodoDto(
                analisis.getPeriodo().getIdPeriodo(),
                analisis.getPeriodo().getMes(),
                analisis.getPeriodo().getAnio()
        );
        return new AnalisisFinancieroIAResponse(
                analisis.getId(),
                analisis.getNegocioId(),
                periodo,
                analisis.getTextoGenerado(),
                analisis.getTipoAlerta(),
                analisis.getFechaGeneracion()
        );
    }
}
