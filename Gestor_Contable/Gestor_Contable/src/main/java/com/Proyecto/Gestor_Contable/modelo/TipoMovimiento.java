package com.Proyecto.Gestor_Contable.modelo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TipoMovimiento implements Serializable {

    @Id
    private String idTipoMovimiento;
    private String nombre;
    private NaturalezaMovimiento naturaleza;

    public static TipoMovimiento nuevo(String nombre, NaturalezaMovimiento naturaleza) {
        return new TipoMovimiento(new ObjectId().toString(), nombre, naturaleza);
    }
}
