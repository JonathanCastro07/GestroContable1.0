package com.Proyecto.Gestor_Contable.Modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;
    private String preguntaSeguridad;
    private String respuestaSeguridad;
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private List<Negocio> negocio = new ArrayList<>();

    public Usuario() {}
    public Usuario(Long idUsuario, String nombre, String correo, String telefono, String password){
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }
}
