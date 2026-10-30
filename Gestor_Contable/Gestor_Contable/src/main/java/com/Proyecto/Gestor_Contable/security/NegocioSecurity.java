package com.Proyecto.Gestor_Contable.security;

import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.repository.PagoPeriodicoRepository;
import com.Proyecto.Gestor_Contable.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("negocioSecurity")
@RequiredArgsConstructor
public class NegocioSecurity {

    @Autowired
    private NegocioRepository negocioRepository;
    @Autowired
    private MovimientoRepository movimientoRepository;
    @Autowired
    private PagoPeriodicoRepository pagoPeriodicoRepository;

    public boolean esDueno(String idNegocio, String correoAutenticado) {
        return negocioRepository.findById(idNegocio)
                .map(negocio -> negocio.getUsuario().getCorreo().equalsIgnoreCase(correoAutenticado))
                .orElse(false);
    }

    public boolean esDuenoDeMovimiento(String idMovimiento, String correoAutenticado) {
        return movimientoRepository.findById(idMovimiento)
                .map(m -> esDueno(m.getNegocioId(), correoAutenticado))
                .orElse(false);
    }

    public boolean esDuenoDePago(String idPago, String correoAutenticado) {
        return pagoPeriodicoRepository.findById(idPago)
                .map(p -> esDueno(p.getNegocioId(), correoAutenticado))
                .orElse(false);
    }
}