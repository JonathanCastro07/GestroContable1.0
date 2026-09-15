package com.Proyecto.Gestor_Contable.Mapper;

import com.Proyecto.Gestor_Contable.DTO.NegocioRequest;
import com.Proyecto.Gestor_Contable.DTO.NegocioResponse;
import com.Proyecto.Gestor_Contable.Modelo.Negocio;
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