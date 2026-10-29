package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.dtos.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.exception.MovimientoFinancieroNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.EmbebidosMapper;
import com.Proyecto.Gestor_Contable.mapper.MovimientoFinancieroMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.Periodo;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Proyecto.Gestor_Contable.dtos.MovimientoResumenResponse;

import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private  MovimientoRepository movimientoRepository;
    @Autowired
    private  MovimientoFinancieroMapper movimientoMapper;
    @Autowired
    private EmbebidosMapper embebidos;

    @Override
    public MovimientoFinancieroResponse registrarMovimiento(MovimientoFinancieroRequest request) {
        MovimientoFinanciero movimiento = movimientoMapper.toEntity(request);
        MovimientoFinanciero guardado = movimientoRepository.save(movimiento);
        return movimientoMapper.toResponse(guardado);
    }

    @Override
    public MovimientoFinancieroResponse editarMovimiento(String id, MovimientoFinancieroRequest request) {
        MovimientoFinanciero existente = movimientoRepository.findById(id)
                .orElseThrow(() -> new MovimientoFinancieroNoEncontradoException("Movimiento no encontrado con id: " + id));

        LocalDateTime fecha = request.fecha().atStartOfDay();
        existente.setMonto(request.monto());
        existente.setFecha(fecha);
        existente.setDescripcion(request.descricion());
        existente.setNegocioId(request.negocioId());
        existente.setTipoMovimiento(embebidos.toTipo(request.tipoMovimiento()));
        existente.setOrigen(embebidos.toOrigen(request.origen()));
        existente.setPeriodo(Periodo.de(fecha));

        MovimientoFinanciero actualizado = movimientoRepository.save(existente);
        return movimientoMapper.toResponse(actualizado);
    }

    @Override
    public void eliminarMovimiento(String id) {
        if (!movimientoRepository.existsById(id)) {
            throw new MovimientoFinancieroNoEncontradoException("Movimiento no encontrado con id: " + id);
        }
        movimientoRepository.deleteById(id);
    }

    @Override
    public List<MovimientoFinancieroResponse> listarPorNegocio(String idNegocio) {
        return movimientoRepository.findByNegocioId(idNegocio)
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    @Override
    public List<MovimientoFinancieroResponse> listarPorNegocioYFecha(String idNegocio, LocalDate desde, LocalDate hasta) {
        return movimientoRepository.findByNegocioIdAndFechaBetween(idNegocio, desde, hasta)
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    @Override
    public List<MovimientoFinancieroResponse> listarPorPeriodo(String idNegocio, String mes, Integer anio) {
        return movimientoRepository.findByNegocioIdAndPeriodo_MesIgnoreCaseAndPeriodo_Anio(idNegocio, mes, anio)
                .stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    @Override
    public MovimientoResumenResponse obtenerResumen(String negocioId, String periodo) {
        LocalDate hasta = LocalDate.now();
        LocalDate desde = switch (periodo.toLowerCase()) {
            case "semanal" -> hasta.minusDays(7);
            case "quincenal" -> hasta.minusDays(15);
            case "mensual" -> hasta.minusDays(30);
            default -> throw new IllegalArgumentException("Período inválido, use: semanal, quincenal o mensual");
        };

        List<MovimientoFinanciero> movimientos = movimientoRepository.findByNegocioId(negocioId)
                .stream()
                .filter(m -> !m.getFecha().toLocalDate().isBefore(desde) && !m.getFecha().toLocalDate().isAfter(hasta))
                .toList();

        double totalIngresos = sumarPorNombreTipo(movimientos, "INGRESO");
        double totalEgresos = sumarPorNombreTipo(movimientos, "EGRESO");
        double totalGastos = sumarPorNombreTipo(movimientos, "GASTO");

        return new MovimientoResumenResponse(
                periodo,
                desde,
                hasta,
                totalIngresos,
                totalEgresos,
                totalIngresos - totalEgresos - totalGastos
        );
    }

    private double sumarPorNombreTipo(List<MovimientoFinanciero> movimientos, String nombreTipo) {
        return movimientos.stream()
                .filter(m -> m.getTipoMovimiento() != null
                        && nombreTipo.equalsIgnoreCase(m.getTipoMovimiento().getNombre()))
                .mapToDouble(MovimientoFinanciero::getMonto)
                .sum();
    }
}

