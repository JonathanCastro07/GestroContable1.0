package com.Proyecto.Gestor_Contable.modelo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Periodo implements Serializable {
    private static final String[] MESES = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    @Id
    private String idPeriodo;
    private String mes;
    private Integer anio;

    public static Periodo de(LocalDateTime fecha) {
        return new Periodo(new ObjectId().toString(), MESES[fecha.getMonthValue() - 1], fecha.getYear());
    }

}
