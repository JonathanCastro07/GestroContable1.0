package com.Proyecto.Gestor_Contable.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "movimiento_financiero")
public class MovimientoFinanciero implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;
    private double monto;
    private LocalDateTime fecha;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "id_negocio")
    private Negocio negocio;
    @ManyToOne
    @JoinColumn(name = "id_tipo")
    private TipoMovimiento tipoMovimiento;
    @ManyToOne
    @JoinColumn(name = "id_origen")
    private Origen origen;
    @ManyToOne
    @JoinColumn(name = "id_periodo")
    private Periodo periodo;

    public MovimientoFinanciero() {}
    public MovimientoFinanciero(Long idMovimiento,
                                double monto, LocalDateTime fecha, String descripcion){
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

}
