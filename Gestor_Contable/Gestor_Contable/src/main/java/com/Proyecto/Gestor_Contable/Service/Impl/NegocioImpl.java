package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.DTO.NegocioRequest;
import com.Proyecto.Gestor_Contable.DTO.NegocioResponse;
import com.Proyecto.Gestor_Contable.Exception.NegocioNoEncontradoException;
import com.Proyecto.Gestor_Contable.Mapper.NegocioMapper;
import com.Proyecto.Gestor_Contable.Modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.Modelo.Negocio;
import com.Proyecto.Gestor_Contable.Modelo.TipoMovimiento;
import com.Proyecto.Gestor_Contable.Repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.Repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.Repository.TipoMovimientoRepository;
import com.Proyecto.Gestor_Contable.Service.NegocioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NegocioImpl implements NegocioServicio {

    private final NegocioRepository negocioRepository;
    private final MovimientoRepository movimientoRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final NegocioMapper negocioMapper;

    @Override
    public NegocioResponse crear(NegocioRequest request) {
        Negocio negocio = negocioMapper.toEntity(request);
        Negocio guardado = negocioRepository.save(negocio);
        return negocioMapper.toResponse(guardado);
    }

    @Override
    public List<NegocioResponse> listarPorUsuario(String idUsuario) {
        return negocioRepository.findByUsuarioId(idUsuario)
                .stream()
                .map(negocioMapper::toResponse)
                .toList();
    }

    @Override
    public NegocioResponse actualizar(String id, NegocioRequest request) {
        Negocio existente = negocioRepository.findById(id)
                .orElseThrow(() -> new NegocioNoEncontradoException("Negocio no encontrado con id: " + id));

        existente.setNombreNegocio(request.nombreNegocio());
        existente.setTipoActividad(request.TipoActividad());
        existente.setCapitalInicial(request.capitalInicial());

        Negocio actualizado = negocioRepository.save(existente);
        return negocioMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!negocioRepository.existsById(id)) {
            throw new NegocioNoEncontradoException("Negocio no encontrado con id: " + id);
        }
        negocioRepository.deleteById(id);
    }

    @Override
    public double calcularUtilidades(String idNegocio) {
        if (!negocioRepository.existsById(idNegocio)) {
            throw new NegocioNoEncontradoException("Negocio no encontrado con id: " + idNegocio);
        }

        double totalIngresos = sumarPorTipo(idNegocio, "INGRESO");
        double totalEgresos = sumarPorTipo(idNegocio, "EGRESO");
        double totalGastos = sumarPorTipo(idNegocio, "GASTO");

        return totalIngresos - totalEgresos - totalGastos;
    }

    @Override
    public Object verResumenFinanciero(String idNegocio) {
        if (!negocioRepository.existsById(idNegocio)) {
            throw new NegocioNoEncontradoException("Negocio no encontrado con id: " + idNegocio);
        }

        double totalIngresos = sumarPorTipo(idNegocio, "INGRESO");
        double totalEgresos = sumarPorTipo(idNegocio, "EGRESO");
        double totalGastos = sumarPorTipo(idNegocio, "GASTO");

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalIngreso", totalIngresos);
        resumen.put("totalEgresos", totalEgresos);
        resumen.put("totalGastos", totalGastos);
        resumen.put("utilidad", totalIngresos - totalEgresos - totalGastos);
        return resumen;
    }

    private double sumarPorTipo(String idNegocio, String nombreTipo) {
        List<TipoMovimiento> tipos = tipoMovimientoRepository.findAll()
                .stream()
                .filter(tipo -> tipo.getNombre().equalsIgnoreCase(nombreTipo))
                .toList();

        double total = 0.0;
        for (TipoMovimiento tipo : tipos) {
            List<MovimientoFinanciero> movimientos =
                    movimientoRepository.findByNegocioIdAndTipoMovimientoId(idNegocio, tipo.getIdTipoMovimiento());
            for (MovimientoFinanciero movimiento : movimientos) {
                total += movimiento.getMonto();
            }
        }
        return total;
    }
}