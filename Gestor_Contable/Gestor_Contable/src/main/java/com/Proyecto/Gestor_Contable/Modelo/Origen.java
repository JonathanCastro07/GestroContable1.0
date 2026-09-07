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
@Document(collection = "origenes")
public class Origen implements Serializable {

    @Id
    private String idOrigen;

    private String nombre;

    private String descripcion;

    private TipoOrigen tipoOrigen;

}