package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.exception.PagoPeriodicoNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.EmbebidosMapper;
import com.Proyecto.Gestor_Contable.mapper.PagoPeriodicoMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.PagoPeriodico;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.PagoPeriodicoRepository;
import com.Proyecto.Gestor_Contable.service.lmpl.LogAuditoriaServiceImpl;
import com.Proyecto.Gestor_Contable.service.lmpl.PagoPeriodicoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoPeriodicoServiceImplTest {

    @Mock PagoPeriodicoRepository pagoRepository;
    @Mock MovimientoRepository movimientoRepository;
    @Mock PagoPeriodicoMapper pagoPeriodicoMapper;
    @Mock EmbebidosMapper embebidos;
    @Mock LogAuditoriaServiceImpl logAuditoria;

    @InjectMocks
    PagoPeriodicoServiceImpl service;

    private PagoPeriodico crearPago() {
        PagoPeriodico pago = new PagoPeriodico();
        pago.setIdPago("p1");
        pago.setNombre("Arriendo");
        pago.setMonto(800000.0);
        pago.setFechaPago(LocalDate.of(2026, 11, 20));
        pago.setNegocioId("n1");
        return pago;
    }

    @Test
    void ejecutarPago_creaUnMovimientoConLosDatosDelPago() {
        when(pagoRepository.findById("p1")).thenReturn(Optional.of(crearPago()));

        service.ejecutarPago("p1");

        ArgumentCaptor<MovimientoFinanciero> captor = ArgumentCaptor.forClass(MovimientoFinanciero.class);
        verify(movimientoRepository).save(captor.capture());
        MovimientoFinanciero movimiento = captor.getValue();

        assertThat(movimiento.getMonto()).isEqualTo(800000.0);
        assertThat(movimiento.getNegocioId()).isEqualTo("n1");
        assertThat(movimiento.getDescripcion()).isEqualTo("Pago periódico: Arriendo");
    }

    @Test
    void ejecutarPago_pasaLaFechaDelPagoAlSiguienteMes() {
        PagoPeriodico pago = crearPago();
        when(pagoRepository.findById("p1")).thenReturn(Optional.of(pago));

        service.ejecutarPago("p1");

        assertThat(pago.getFechaPago()).isEqualTo(LocalDate.of(2026, 12, 20));
        verify(pagoRepository).save(pago);
    }

    @Test
    void ejecutarPago_queNoExiste_lanzaError() {
        when(pagoRepository.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.ejecutarPago("x"))
                .isInstanceOf(PagoPeriodicoNoEncontradoException.class);

        verify(movimientoRepository, never()).save(any());
    }

    @Test
    void eliminar_borraElPago() {
        when(pagoRepository.findById("p1")).thenReturn(Optional.of(crearPago()));

        service.eliminar("p1");

        verify(pagoRepository).deleteById("p1");
    }
}
