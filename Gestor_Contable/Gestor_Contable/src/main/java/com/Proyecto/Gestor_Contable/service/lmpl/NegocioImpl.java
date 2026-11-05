package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.dtos.NegocioRequest;
import com.Proyecto.Gestor_Contable.dtos.NegocioResponse;
import com.Proyecto.Gestor_Contable.exception.NegocioNoEncontradoException;
import com.Proyecto.Gestor_Contable.mapper.NegocioMapper;
import com.Proyecto.Gestor_Contable.modelo.MovimientoFinanciero;
import com.Proyecto.Gestor_Contable.modelo.Negocio;
import com.Proyecto.Gestor_Contable.repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.service.NegocioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NegocioImpl implements NegocioServicio {
    @Autowired
    private  NegocioRepository negocioRepository;
    @Autowired
    private  MovimientoRepository movimientoRepository;
    @Autowired
    private  NegocioMapper negocioMapper;
    @Autowired
    private  LogAuditoriaServiceImpl logAuditoria;

    @Override
    public NegocioResponse crear(NegocioRequest request) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Negocio existente = negocioRepository.findFirstByUsuario_Correo(correo)
                .orElseThrow(() -> new NegocioNoEncontradoException("No se encontró información del usuario autenticado"));

        Negocio nuevo = negocioMapper.toEntity(request);
        nuevo.setUsuario(existente.getUsuario());

        Negocio guardado = negocioRepository.save(nuevo);
        logAuditoria.registrar(guardado.getIdNegocio(),
                "NEGOCIO_CREADO", "Negocio: " + guardado.getNombreNegocio());
        return negocioMapper.toResponse(guardado);
    }

    @Override
    public List<NegocioResponse> listarPorUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return negocioRepository.findByUsuario_Correo(correo)
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
        logAuditoria.registrar(actualizado.getIdNegocio(),
                "NEGOCIO_EDITADO", "Negocio: " + actualizado.getNombreNegocio());
        return negocioMapper.toResponse(actualizado);
    }

    @Override
    public void eliminar(String id) {
        if (!negocioRepository.existsById(id)) {
            throw new NegocioNoEncontradoException("Negocio no encontrado con id: " + id);
        }
        negocioRepository.deleteById(id);
        logAuditoria.registrar(id, "NEGOCIO_ELIMINADO", "Negocio eliminado");
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
        return movimientoRepository.findByNegocioId(idNegocio)
                .stream()
                .filter(m -> m.getTipoMovimiento() != null
                        && nombreTipo.equalsIgnoreCase(m.getTipoMovimiento().getNombre()))
                .mapToDouble(MovimientoFinanciero::getMonto)
                .sum();
    }
}