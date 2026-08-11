package com.Proyecto.Gestor_Contable.Modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name = "negocios")
public class Negocio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNegocio;
    private String nombreNegocio;
    private String tipoActividad;
    private double capitalInicial;
    private String rolPropietario;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Negocio() {}
    public Negocio(Long idNegocio, String nombreNegocio, String tipoActividad, double capitalInicial, String rolPropietario){
        this.idNegocio = idNegocio;
        this.nombreNegocio = nombreNegocio;
        this.tipoActividad = tipoActividad;
        this.capitalInicial = capitalInicial;
        this.rolPropietario = rolPropietario;
    }
}
