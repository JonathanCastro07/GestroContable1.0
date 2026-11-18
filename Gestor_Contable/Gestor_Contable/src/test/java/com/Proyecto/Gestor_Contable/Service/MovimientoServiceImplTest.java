package com.Proyecto.Gestor_Contable.Service;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;
import com.Proyecto.Gestor_Contable.exception.MovimientoFinancieroNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.EmbebidosMapper;
import com.Proyecto.Gestor_Contable.mapper.MovimientoFinancieroMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.NaturalezaMovimiento;
import com.Proyecto.Gestor_Contable.modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.service.lmpl.LogAuditoriaServiceImpl;
import com.Proyecto.Gestor_Contable.service.lmpl.MovimientoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock MovimientoRepository movimientoRepository;
    @Mock MovimientoFinancieroMapper movimientoMapper;
    @Mock EmbebidosMapper embebidos;
    @Mock LogAuditoriaServiceImpl logAuditoria;

    @InjectMocks
    MovimientoServiceImpl service;

    private MovimientoFinanciero crearMovimiento(String tipo, double monto) {
        MovimientoFinanciero m = new MovimientoFinanciero();
        m.setIdMovimiento("m1");
        m.setMonto(monto);
        m.setFecha(LocalDateTime.now());
        m.setNegocioId("n1");
        m.setTipoMovimiento(TipoMovimiento.nuevo(tipo, NaturalezaMovimiento.CREDITO));
        return m;
    }

    @Test
    void registrarMovimiento_guardaElMovimiento() {
        MovimientoFinancieroRequest request = new MovimientoFinancieroRequest(
                50000, LocalDateTime.now(), "Venta", "n1", null, null);
        MovimientoFinanciero movimiento = crearMovimiento("INGRESO", 50000);
        MovimientoFinancieroResponse response = new MovimientoFinancieroResponse(
                "m1", 50000, movimiento.getFecha(), "Venta", "n1", null, null, null);

        when(movimientoMapper.toEntity(request)).thenReturn(movimiento);
        when(movimientoRepository.save(movimiento)).thenReturn(movimiento);
        when(movimientoMapper.toResponse(movimiento)).thenReturn(response);

        MovimientoFinancieroResponse resultado = service.registrarMovimiento(request);

        assertThat(resultado.idMovimiento()).isEqualTo("m1");
        assertThat(resultado.monto()).isEqualTo(50000);
        verify(movimientoRepository).save(movimiento);
    }

    @Test
    void eliminarMovimiento_borraElMovimiento() {
        when(movimientoRepository.findById("m1")).thenReturn(Optional.of(crearMovimiento("INGRESO", 1000)));

        service.eliminarMovimiento("m1");

        verify(movimientoRepository).deleteById("m1");
    }

    @Test
    void eliminarMovimiento_queNoExiste_lanzaError() {
        when(movimientoRepository.findById("x")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminarMovimiento("x"))
                .isInstanceOf(MovimientoFinancieroNoEncontradoException.class);

        verify(movimientoRepository, never()).deleteById("x");
    }

    @Test
    void obtenerResumen_calculaIngresosEgresosYGanancias() {
        when(movimientoRepository.findByNegocioId("n1")).thenReturn(List.of(
                crearMovimiento("INGRESO", 1000),
                crearMovimiento("EGRESO", 300),
                crearMovimiento("GASTO", 200)
        ));

        MovimientoResumenResponse resumen = service.obtenerResumen("n1", "mensual");

        assertThat(resumen.totalIngresos()).isEqualTo(1000);
        assertThat(resumen.totalEgresos()).isEqualTo(300);
        assertThat(resumen.totalGanancias()).isEqualTo(500);
    }

    @Test
    void obtenerResumen_conPeriodoInvalido_lanzaError() {
        assertThatThrownBy(() -> service.obtenerResumen("n1", "anual"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
