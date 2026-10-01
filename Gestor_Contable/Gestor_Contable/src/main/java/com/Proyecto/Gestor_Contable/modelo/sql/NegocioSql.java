package com.Proyecto.Gestor_Contable.modelo.sql;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "negocios_sql")
public class NegocioSql {
    @Id
    private String idNegocio;
    private String nombreNegocio;
    private String tipoActividad;
    private double capitalInicial;
    private String rolPropietario;
    private String usuarioId;
}