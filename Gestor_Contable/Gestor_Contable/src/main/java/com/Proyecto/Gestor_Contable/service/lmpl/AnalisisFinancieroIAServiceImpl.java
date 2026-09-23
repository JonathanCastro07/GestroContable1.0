package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;
import com.Proyecto.Gestor_Contable.mapper.AnalisisFinancieroIAMapper;
import com.Proyecto.Gestor_Contable.modelo.AnalisisFinancieroIA;
import com.Proyecto.Gestor_Contable.repository.AnalisisFinancieroIARepository;
import com.Proyecto.Gestor_Contable.service.AnalisisFinancieroIAService;
import com.Proyecto.Gestor_Contable.service.ClienteIAService;
import com.Proyecto.Gestor_Contable.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalisisFinancieroIAServiceImpl implements AnalisisFinancieroIAService {

    @Autowired
    private AnalisisFinancieroIARepository analisisRepository;

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private ClienteIAService clienteIAService;

    @Override
    public AnalisisFinancieroIAResponse generarAnalisis(String negocioId, String periodo) {

        MovimientoResumenResponse resumen = movimientoService.obtenerResumen(negocioId, periodo);

        String prompt = construirPrompt(resumen, periodo);

        String textoGenerado = clienteIAService.generarTexto(prompt);

        AnalisisFinancieroIA analisis = new AnalisisFinancieroIA();
        analisis.setNegocioId(negocioId);
        analisis.setPeriodo(periodo);
        analisis.setTextoGenerado(textoGenerado);
        analisis.setTipoAlerta(determinarTipoAlerta(resumen));
        analisis.setFechaGeneracion(LocalDateTime.now());

        AnalisisFinancieroIA guardado = analisisRepository.save(analisis);
        return AnalisisFinancieroIAMapper.toResponse(guardado);
    }

    @Override
    public List<AnalisisFinancieroIAResponse> listarPorNegocio(String negocioId) {
        return analisisRepository.findByNegocioId(negocioId)
                .stream()
                .map(AnalisisFinancieroIAMapper::toResponse)
                .toList();
    }

    private String construirPrompt(MovimientoResumenResponse resumen, String periodo) {
        return "Analiza estos datos financieros del periodo " + periodo + ": " +
                "Ingresos: " + resumen.totalIngresos() + ", " +
                "Egresos: " + resumen.totalEgresos() + ", " +
                "Ganancia: " + resumen.totalGanancias() +
                ". Da una recomendacion breve en espanol.";
    }

    private String determinarTipoAlerta(MovimientoResumenResponse resumen) {
        if (resumen.totalEgresos() > resumen.totalIngresos()) {
            return "GASTO_ELEVADO";
        }
        return "TENDENCIA_POSITIVA";
    }
}
