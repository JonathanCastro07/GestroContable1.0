package com.Proyecto.Gestor_Contable.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "periodos")
public class Periodo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPeriodo;
    @Column(name = ("mes"))
    private String mes;

    private Integer anio;

    public Periodo() {
    }

    public Periodo(Long idPeriodo, String mes, Integer anio) {
        this.idPeriodo = idPeriodo;
        this.mes = mes;
        this.anio = anio;
    }
}
