package com.Proyecto.Gestor_Contable.Modelo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "origenes")
public class Origen implements Serializable {

    @Id
    private String idOrigen;

    private String nombre;

    private String descripcion;

    private TipoOrigen tipoOrigen;

    public Origen() {}

    public Origen(String idOrigen, String nombre, String descripcion, TipoOrigen tipoOrigen) {
        this.idOrigen = idOrigen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoOrigen = tipoOrigen;
    }
}