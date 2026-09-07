package com.Proyecto.Gestor_Contable.Modelo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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



}
