package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import org.springframework.stereotype.Component;

@Component
public class NegocioMapper {

    public Negocio toEntity(NegocioRequest request) {
        Negocio negocio = new Negocio();
        negocio.setNombreNegocio(request.nombreNegocio());
        negocio.setTipoActividad(request.TipoActividad());
        negocio.setCapitalInicial(request.capitalInicial());
        return negocio;
    }

    public NegocioResponse toResponse(Negocio negocio) {
        return new NegocioResponse(
                negocio.getIdNegocio(),
                negocio.getNombreNegocio(),
                negocio.getTipoActividad(),
                negocio.getCapitalInicial()
        );
    }
}