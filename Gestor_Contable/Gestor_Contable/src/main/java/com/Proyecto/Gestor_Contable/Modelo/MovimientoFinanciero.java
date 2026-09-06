package com.Proyecto.Gestor_Contable.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Document(collection = "movimiento_financiero")
public class MovimientoFinanciero implements Serializable {

    @Id
    private String idMovimiento;
    private double monto;
    private LocalDateTime fecha;
    private String descripcion;
    private String negocioId;
    private String tipoMovimientoId;
    private String origenId;
    private String periodoId;

    public MovimientoFinanciero() {}
    public MovimientoFinanciero(String idMovimiento, double monto, LocalDateTime fecha, String descripcion){
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

}
