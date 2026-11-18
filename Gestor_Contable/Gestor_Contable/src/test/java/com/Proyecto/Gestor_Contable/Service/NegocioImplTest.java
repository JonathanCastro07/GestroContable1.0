package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;
import com.Proyecto.Gestor_Contable.exception.NegocioNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.NegocioMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.NaturalezaMovimiento;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.service.lmpl.LogAuditoriaServiceImpl;
import com.Proyecto.Gestor_Contable.service.lmpl.NegocioImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NegocioImplTest {

    @Mock NegocioRepository negocioRepository;
    @Mock MovimientoRepository movimientoRepository;
    @Mock LogAuditoriaServiceImpl logAuditoria;

    @Spy
    NegocioMapper negocioMapper = new NegocioMapper();

    @InjectMocks
    NegocioImpl service;

    private MovimientoFinanciero crearMovimiento(String tipo, double monto) {
        MovimientoFinanciero m = new MovimientoFinanciero();
        m.setMonto(monto);
        m.setNegocioId("n1");
        m.setTipoMovimiento(TipoMovimiento.nuevo(tipo, NaturalezaMovimiento.CREDITO));
        return m;
    }

    @Test
    void calcularUtilidades_restaEgresosYGastosALosIngresos() {
        when(negocioRepository.existsById("n1")).thenReturn(true);
        when(movimientoRepository.findByNegocioId("n1")).thenReturn(List.of(
                crearMovimiento("INGRESO", 5000),
                crearMovimiento("EGRESO", 2000),
                crearMovimiento("GASTO", 1000)
        ));

        double utilidad = service.calcularUtilidades("n1");

        assertThat(utilidad).isEqualTo(2000);
    }

    @Test
    void calcularUtilidades_deNegocioQueNoExiste_lanzaError() {
        when(negocioRepository.existsById("x")).thenReturn(false);

        assertThatThrownBy(() -> service.calcularUtilidades("x"))
                .isInstanceOf(NegocioNoEncontradoException.class);
    }

    @Test
    void actualizar_cambiaLosDatosDelNegocio() {
        Negocio negocio = new Negocio();
        negocio.setIdNegocio("n1");
        negocio.setNombreNegocio("Tienda vieja");
        when(negocioRepository.findById("n1")).thenReturn(Optional.of(negocio));
        when(negocioRepository.save(any(Negocio.class))).thenAnswer(inv -> inv.getArgument(0));

        NegocioResponse response = service.actualizar("n1", new NegocioRequest("Tienda nueva", "COMERCIO", 500000));

        assertThat(response.nombre()).isEqualTo("Tienda nueva");
        assertThat(negocio.getCapitalInicial()).isEqualTo(500000);
    }

    @Test
    void eliminar_negocioQueNoExiste_lanzaError() {
        when(negocioRepository.existsById("x")).thenReturn(false);

        assertThatThrownBy(() -> service.eliminar("x"))
                .isInstanceOf(NegocioNoEncontradoException.class);

        verify(negocioRepository, never()).deleteById("x");
    }
}
