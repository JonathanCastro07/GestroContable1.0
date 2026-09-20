package com.Proyecto.Gestor_Contable.Mapper;

import com.Proyecto.Gestor_Contable.DTO.RegistroRequest;
import com.Proyecto.Gestor_Contable.DTO.UsuarioResponse;
import com.Proyecto.Gestor_Contable.Modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

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