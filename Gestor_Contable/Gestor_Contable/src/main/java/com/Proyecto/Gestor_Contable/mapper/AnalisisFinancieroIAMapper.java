package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import com.Proyecto.Gestor_Contable.modelo.AnalisisFinancieroIA;

public class AnalisisFinancieroIAMapper {

    public  static AnalisisFinancieroIAResponse toResponse(AnalisisFinancieroIA response){
        return new AnalisisFinancieroIAResponse(
                response.getId(),
                response.getNegocioId(),
                response.getPeriodo(),
                response.getTextoGenerado(),
                response.getTipoAlerta(),
                response.getFechaGeneracion()
        );
    }
}
