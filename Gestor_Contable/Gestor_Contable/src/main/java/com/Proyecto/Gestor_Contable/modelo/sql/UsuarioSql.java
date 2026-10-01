package com.Proyecto.Gestor_Contable.modelo.sql;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios_sql")
public class UsuarioSql {
    @Id
    private String idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;
    private String preguntaSeguridad;
    private String respuestaSeguridad;
}