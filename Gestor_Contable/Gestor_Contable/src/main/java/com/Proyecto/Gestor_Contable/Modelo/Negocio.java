package com.Proyecto.Gestor_Contable.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "negocios")
public class Negocio {
    @Id
    private String idNegocio;
    private String nombreNegocio;
    private String tipoActividad;
    private double capitalInicial;
    private String rolPropietario;
    private String usuarioId;
    public Negocio() {}
    public Negocio(String idNegocio, String nombreNegocio, String tipoActividad, double capitalInicial, String rolPropietario){
        this.idNegocio = idNegocio;
        this.nombreNegocio = nombreNegocio;
        this.tipoActividad = tipoActividad;
        this.capitalInicial = capitalInicial;
        this.rolPropietario = rolPropietario;
    }
}
