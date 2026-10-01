package com.Proyecto.Gestor_Contable.modelo.sql;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "periodos_sql")
public class PeriodoSql {
    @Id
    private String idPeriodo;
    private String mes;
    private Integer anio;
}
