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
public class Origen implements Serializable {

    @Id
    private String idOrigen;
    private String nombre;
    private TipoOrigen tipoOrigen;

    public static Origen nuevo(String nombre, TipoOrigen tipoOrigen) {
        return new Origen(new ObjectId().toString(), nombre, tipoOrigen);
    }
}