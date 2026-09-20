package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.DTO.MovimientoFinancieroRequest;
import com.Proyecto.Gestor_Contable.DTO.MovimientoFinancieroResponse;
import com.Proyecto.Gestor_Contable.Exception.MovimientoFinancieroNoEncontradoException;
import com.Proyecto.Gestor_Contable.Mapper.MovimientoFinancieroMapper;
import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.Modelo.Periodo;
import com.Proyecto.Gestor_Contable.Repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.Repository.PeriodoRepository;
import com.Proyecto.Gestor_Contable.Service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Proyecto.Gestor_Contable.DTO.MovimientoResumenResponse;
import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.Repository.TipoMovimientoRepository;

import java.util.Map;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final MovimientoRepository movimientoRepository;
    private final PeriodoRepository periodoRepository;
    private final MovimientoFinancieroMapper movimientoMapper;

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

        existente.setMonto(request.monto());
        existente.setFecha(request.fecha().atStartOfDay());
        existente.setDescripcion(request.descricion());
        existente.setNegocioId(request.negocioId());
        existente.setTipoMovimientoId(request.tipoId());
        existente.setOrigenId(request.origenId());
        existente.setPeriodoId(request.periodoId());

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
        Optional<Periodo> periodo = periodoRepository.findByMesAndAnio(mes, anio);

        if (periodo.isEmpty()) {
            return List.of();
        }

        return movimientoRepository.findByNegocioIdAndPeriodoId(idNegocio, periodo.get().getIdPeriodo())
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

        Map<String, String> nombrePorTipoId = tipoMovimientoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(TipoMovimiento::getIdTipoMovimiento, TipoMovimiento::getNombre));

        double totalIngresos = sumarPorNombreTipo(movimientos, nombrePorTipoId, "INGRESO");
        double totalEgresos = sumarPorNombreTipo(movimientos, nombrePorTipoId, "EGRESO");
        double totalGastos = sumarPorNombreTipo(movimientos, nombrePorTipoId, "GASTO");

        return new MovimientoResumenResponse(
                periodo,
                desde,
                hasta,
                totalIngresos,
                totalEgresos,
                totalIngresos - totalEgresos - totalGastos
        );
    }

    private double sumarPorNombreTipo(List<MovimientoFinanciero> movimientos, Map<String, String> nombrePorTipoId, String nombreTipo) {
        return movimientos.stream()
                .filter(m -> nombreTipo.equalsIgnoreCase(nombrePorTipoId.get(m.getTipoMovimientoId())))
                .mapToDouble(MovimientoFinanciero::getMonto)
                .sum();
    }
}

