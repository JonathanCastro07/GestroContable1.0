package com.Proyecto.Gestor_Contable.Modelo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "origenes")
public class Origen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idOrigen;

    private String nombre;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoOrigen tipoOrigen;

    public Origen() {}

    public Origen(Long idOrigen, String nombre, String descripcion, TipoOrigen tipoOrigen) {
        this.idOrigen = idOrigen;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoOrigen = tipoOrigen;
    }
}