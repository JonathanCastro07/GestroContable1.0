package com.Proyecto.Gestor_Contable.Mapper;


import com.Proyecto.Gestor_Contable.DTO.OrigenRequest;
import com.Proyecto.Gestor_Contable.DTO.OrigenResponse;
import com.Proyecto.Gestor_Contable.Modelo.Origen;
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
