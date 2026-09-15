package com.Proyecto.Gestor_Contable.Mapper;

import com.Proyecto.Gestor_Contable.DTO.PeriodoRequest;
import com.Proyecto.Gestor_Contable.DTO.PeriodoResponse;
import com.Proyecto.Gestor_Contable.Modelo.Periodo;
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