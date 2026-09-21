package com.Proyecto.Gestor_Contable.mapper;


import com.Proyecto.Gestor_Contable.dtos.OrigenRequest;
import com.Proyecto.Gestor_Contable.dtos.OrigenResponse;
import com.Proyecto.Gestor_Contable.modelo.Origen;
import org.springframework.stereotype.Component;

@Component
public class OrigenMapper {
    public Origen toEntity(OrigenRequest request){
        Origen origen = new Origen();
        origen.setDescripcion(request.descripcion());
        origen.setTipoOrigen(request.tipoOrigen());
        return origen;

    }

    public OrigenResponse toResponse(Origen origen){
        return new OrigenResponse(
                origen.getIdOrigen(),
                origen.getDescripcion(),
                origen.getTipoOrigen()
        );
    }
}
