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
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "periodos")
public class Periodo implements Serializable {
    @Id
    private String idPeriodo;
    private String mes;

    private Integer anio;


}
