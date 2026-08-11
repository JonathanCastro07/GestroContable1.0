package com.Proyecto.Gestor_Contable.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pagos_periodicos")
public class PagoPeriodico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPago;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double monto;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Column(nullable = false)
    private boolean activo = true;

    @Column
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_negocio", nullable = false)
    private Negocio negocio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo")
    private TipoMovimiento tipoMovimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_origen")
    private Origen origen;

    public PagoPeriodico() {}
}
