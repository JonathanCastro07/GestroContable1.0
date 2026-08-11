package com.Proyecto.Gestor_Contable.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tipos_movimiento")
public class TipoMovimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idTipo;

    private String nombre;  // INGRESO, EGRESO, GASTO

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NaturalezaMovimiento naturaleza;  // DEBITO o CREDITO

    public TipoMovimiento() {}

    public TipoMovimiento(Long idTipo, String nombre, NaturalezaMovimiento naturaleza) {
        this.idTipo = idTipo;
        this.nombre = nombre;
        this.naturaleza = naturaleza;
    }
}
