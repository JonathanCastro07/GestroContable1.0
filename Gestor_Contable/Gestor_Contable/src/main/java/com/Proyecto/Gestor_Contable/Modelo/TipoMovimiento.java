package com.Proyecto.Gestor_Contable.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Document(collection = "tipos_movimiento")
public class TipoMovimiento implements Serializable {

    @Id
    private String idTipoMovimiento;

    private String nombre;  // INGRESO, EGRESO, GASTO

    private NaturalezaMovimiento naturaleza;  // DEBITO o CREDITO

    public TipoMovimiento() {}

    public TipoMovimiento(String idTipoMovimiento, String nombre, NaturalezaMovimiento naturaleza) {
        this.idTipoMovimiento = idTipoMovimiento;
        this.nombre = nombre;
        this.naturaleza = naturaleza;
    }
}
