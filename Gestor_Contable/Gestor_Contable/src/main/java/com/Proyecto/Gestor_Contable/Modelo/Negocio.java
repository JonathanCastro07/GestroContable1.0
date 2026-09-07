package com.Proyecto.Gestor_Contable.Modelo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "negocios")
public class Negocio {
    @Id
    private String idNegocio;
    private String nombreNegocio;
    private String tipoActividad;
    private double capitalInicial;
    private String rolPropietario;
    private String usuarioId;

}
