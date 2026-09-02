package com.Proyecto.Gestor_Contable.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "periodos")
public class Periodo implements Serializable {
    @Id
    private String idPeriodo;
    private String mes;

    private Integer anio;

    public Periodo() {
    }

    public Periodo(String idPeriodo, String mes, Integer anio) {
        this.idPeriodo = idPeriodo;
        this.mes = mes;
        this.anio = anio;
    }
}
