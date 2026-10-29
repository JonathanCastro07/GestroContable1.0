package com.Proyecto.Gestor_Contable.security;

import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("negocioSecurity")
@RequiredArgsConstructor
public class NegocioSecurity {
    private final NegocioRepository negocioRepository;

    public boolean esDueno(String idNegocio, String correoAutenticado) {
        return negocioRepository.findById(idNegocio)
                .map(negocio -> negocio.getUsuario().getCorreo().equalsIgnoreCase(correoAutenticado))
                .orElse(false);
    }
}