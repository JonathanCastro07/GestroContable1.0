package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.PeriodoRequest;
import com.Proyecto.Gestor_Contable.dtos.PeriodoResponse;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import org.springframework.stereotype.Component;

@Component
public class PeriodoMapper {

    public Periodo toEntity(PeriodoRequest request) {
        Periodo periodo = new Periodo();
        periodo.setMes(request.mes());
        periodo.setAnio(request.anio());
        return periodo;
    }

    public PeriodoResponse toResponse(Periodo periodo) {
        return new PeriodoResponse(
                periodo.getIdPeriodo(),
                periodo.getMes(),
                periodo.getAnio()
        );
    }
}