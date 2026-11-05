package com.Proyecto.Gestor_Contable.Service;


import com.Proyecto.Gestor_Contable.dtos.AnalisisFinancieroIAResponse;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;
import com.Proyecto.Gestor_Contable.modelo.AnalisisFinancieroIA;
import com.Proyecto.Gestor_Contable.repository.AnalisisFinancieroIARepository;
import com.Proyecto.Gestor_Contable.service.ClienteIAService;
import com.Proyecto.Gestor_Contable.service.MovimientoService;
import com.Proyecto.Gestor_Contable.service.lmpl.AnalisisFinancieroIAServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalisisFinancieroIAServiceImplTest {

    @Mock AnalisisFinancieroIARepository analisisRepository;
    @Mock MovimientoService movimientoService;
    @Mock ClienteIAService clienteIAService;

    @InjectMocks
    AnalisisFinancieroIAServiceImpl service;

    private MovimientoResumenResponse resumen(double ingresos, double egresos) {
        return new MovimientoResumenResponse("mensual",
                LocalDate.of(2026, 9, 20), LocalDate.of(2026, 10, 20),
                ingresos, egresos, ingresos - egresos);
    }

    @Test
    void generarAnalisis_conGanancia_guardaTendenciaPositiva() {
        when(movimientoService.obtenerResumen("n1", "mensual")).thenReturn(resumen(1000, 400));
        when(clienteIAService.generarTexto(anyString())).thenReturn("Vas muy bien");
        when(analisisRepository.save(any(AnalisisFinancieroIA.class))).thenAnswer(inv -> {
            AnalisisFinancieroIA a = inv.getArgument(0);
            a.setId("a1");
            return a;
        });

        AnalisisFinancieroIAResponse response = service.generarAnalisis("n1", "mensual");

        assertThat(response.id()).isEqualTo("a1");
        assertThat(response.negocioid()).isEqualTo("n1");
        assertThat(response.textoGenerado()).isEqualTo("Vas muy bien");
        assertThat(response.tipoAlerta()).isEqualTo("TENDENCIA_POSITIVA");
        assertThat(response.fechaGeneracion()).isNotNull();
    }

    @Test
    void generarAnalisis_conEgresosMayores_guardaGastoElevado() {
        when(movimientoService.obtenerResumen("n1", "semanal")).thenReturn(resumen(200, 900));
        when(clienteIAService.generarTexto(anyString())).thenReturn("Reduce gastos");
        when(analisisRepository.save(any(AnalisisFinancieroIA.class))).thenAnswer(inv -> inv.getArgument(0));

        AnalisisFinancieroIAResponse response = service.generarAnalisis("n1", "semanal");

        assertThat(response.tipoAlerta()).isEqualTo("GASTO_ELEVADO");
    }

    @Test
    void generarAnalisis_enviaPromptConLosTotalesAlaIA() {
        when(movimientoService.obtenerResumen("n1", "mensual")).thenReturn(resumen(1000, 400));
        when(clienteIAService.generarTexto(anyString())).thenReturn("ok");
        when(analisisRepository.save(any(AnalisisFinancieroIA.class))).thenAnswer(inv -> inv.getArgument(0));

        service.generarAnalisis("n1", "mensual");

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(clienteIAService).generarTexto(prompt.capture());
        assertThat(prompt.getValue())
                .contains("mensual")
                .contains("1000.0")
                .contains("400.0");
    }

    @Test
    void listarPorNegocio_devuelveLosAnalisisDelNegocio() {
        AnalisisFinancieroIA a = new AnalisisFinancieroIA();
        a.setId("a1");
        a.setNegocioId("n1");
        a.setTextoGenerado("texto");
        when(analisisRepository.findByNegocioId("n1")).thenReturn(List.of(a));

        List<AnalisisFinancieroIAResponse> lista = service.listarPorNegocio("n1");

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).id()).isEqualTo("a1");
        verify(analisisRepository).findByNegocioId("n1");
    }
}
