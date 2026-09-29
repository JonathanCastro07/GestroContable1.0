package com.Proyecto.Gestor_Contable.modelo.sql;

import com.Proyecto.Gestor_Contable.modelo.NaturalezaMovimiento;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_movimiento_sql")
public class TipoMovimientoSql {
    @Id
    private String idTipoMovimiento;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private NaturalezaMovimiento naturaleza;
}