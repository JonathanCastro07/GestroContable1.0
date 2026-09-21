package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.UsuarioResponse;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class MapperUsuario {

    public Usuario toEntity(RegistroRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.Correo());
        usuario.setPassword(request.password());
        usuario.setPreguntaSeguridad(request.preguntaSeguridad());
        return usuario;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo()
        );
    }
}