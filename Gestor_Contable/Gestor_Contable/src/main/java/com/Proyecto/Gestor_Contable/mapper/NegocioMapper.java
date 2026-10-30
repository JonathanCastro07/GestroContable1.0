package com.Proyecto.Gestor_Contable.mapper;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;
import com.Proyecto.Gestor_Contable.dtos.RegistroRequest;
import com.Proyecto.Gestor_Contable.dtos.RegistroResponse;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.modelo.Usuario;
import org.springframework.stereotype.Component;

@Component
public class NegocioMapper {

    public Negocio toEntity(NegocioRequest request) {
        Negocio negocio = new Negocio();
        negocio.setNombreNegocio(request.nombreNegocio());
        negocio.setTipoActividad(request.TipoActividad());
        negocio.setCapitalInicial(request.capitalInicial());
        return negocio;
    }

    public Negocio toEntityConUsuario(RegistroRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setPassword(request.password());
        usuario.setTelefono(request.telefono());
        usuario.setPreguntaSeguridad(request.preguntaSeguridad());
        usuario.setRespuestaSeguridad(request.respuestaSeguridad());

        Negocio negocio = new Negocio();
        negocio.setNombreNegocio(request.nombreNegocio());
        negocio.setTipoActividad(request.tipoActividad());
        negocio.setCapitalInicial(request.capitalInicial());
        negocio.setRolPropietario(request.rolPropietario());
        negocio.setUsuario(usuario);
        return negocio;
    }

    public NegocioResponse toResponse(Negocio negocio) {
        return new NegocioResponse(
                negocio.getIdNegocio(),
                negocio.getNombreNegocio(),
                negocio.getTipoActividad(),
                negocio.getCapitalInicial(),
                negocio.getRolPropietario()
        );
    }

    public RegistroResponse toRegistroResponse(Negocio negocio) {
        return new RegistroResponse(
                negocio.getIdNegocio(),
                negocio.getUsuario().getNombre(),
                negocio.getUsuario().getCorreo(),
                negocio.getNombreNegocio()
        );
    }
}