package com.Proyecto.Gestor_Contable.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovimientoDTO {
    private Long idMovimiento;
    private double monto;
    private LocalDate fecha;
    private String tipo;
    private String origen;
}
