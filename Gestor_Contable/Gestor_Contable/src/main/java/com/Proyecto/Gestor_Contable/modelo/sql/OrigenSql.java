package com.Proyecto.Gestor_Contable.modelo.sql;

import com.Proyecto.Gestor_Contable.modelo.TipoOrigen;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "origenes_sql")
public class OrigenSql {

    @Id
    private String idOrigen;

    private String nombre;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoOrigen tipoOrigen;
}