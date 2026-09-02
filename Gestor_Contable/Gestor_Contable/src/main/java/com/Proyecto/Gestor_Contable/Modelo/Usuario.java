package com.Proyecto.Gestor_Contable.Modelo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


import java.io.Serializable;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario implements Serializable {

    @Id
    private String idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;
    private String preguntaSeguridad;
    private String respuestaSeguridad;

    public Usuario() {}
    public Usuario(String idUsuario, String nombre, String correo, String telefono, String password){         this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }
}
