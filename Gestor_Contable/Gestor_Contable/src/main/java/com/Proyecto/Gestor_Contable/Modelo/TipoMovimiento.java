package com.Proyecto.Gestor_Contable.Modelo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "tipos_movimiento")
public class TipoMovimiento implements Serializable {

    @Id
    private String idTipoMovimiento;

    private String nombre;  // INGRESO, EGRESO, GASTO

    private NaturalezaMovimiento naturaleza;  // DEBITO o CREDITO


}
