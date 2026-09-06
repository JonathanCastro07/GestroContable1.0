package com.Proyecto.Gestor_Contable.Modelo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Document(collection= "pagos_periodicos")
public class PagoPeriodico implements Serializable {

    @Id
    private String idPago;
    private String nombre;
    private Double monto;
    private LocalDate fechaPago;
    private boolean activo = true;
    private String descripcion;
    private String negocioId;
    private String tipoMovimientoId;
    private String origenId;

    public PagoPeriodico() {}
}
