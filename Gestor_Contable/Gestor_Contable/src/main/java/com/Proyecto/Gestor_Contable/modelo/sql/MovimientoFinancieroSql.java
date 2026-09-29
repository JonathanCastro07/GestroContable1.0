package com.Proyecto.Gestor_Contable.modelo.sql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movimientos_financieros_sql")
public class MovimientoFinancieroSql {
    @Id
    private String idMovimiento;
    private double monto;
    private LocalDateTime fecha;
    private String descripcion;
    private String negocioId;
    private String tipoMovimientoId;
    private String origenId;
    private String periodoId;
}