package com.Proyecto.Gestor_Contable.modelo.sql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos_periodicos_sql")
public class PagoPeriodicoSql {
    @Id
    private String idPago;
    private String nombre;
    private Double monto;
    private LocalDate fechaPago;
    private boolean activo;
    private String descripcion;
    private String negocioId;
    private String tipoMovimientoId;
    private String origenId;
}