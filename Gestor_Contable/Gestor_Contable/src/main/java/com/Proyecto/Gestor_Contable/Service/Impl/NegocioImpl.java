package com.Proyecto.Gestor_Contable.Service.Impl;

import com.Proyecto.Gestor_Contable.Modelo.Negocio;
import com.Proyecto.Gestor_Contable.Repository.MovimientoRepository;
import com.Proyecto.Gestor_Contable.Repository.NegocioRepository;
import com.Proyecto.Gestor_Contable.Service.NegocioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NegocioImpl  implements NegocioServicio {
    private final NegocioRepository negocioRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public Negocio crear(Negocio negocio) {
        return negocioRepository.save(negocio);
    }

    @Override
    public List<Negocio> listarPorUsuario(Long idUsuario) {
        return negocioRepository.findByUsuarioIdUsuario(idUsuario);
    }

    @Override
    public Negocio actualizar(Long id, Negocio negocio) {
        Negocio negocioExistente = negocioRepository.findById(id).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        negocioExistente.setUsuario(negocio.getUsuario());
        negocioExistente.setNombreNegocio(negocio.getNombreNegocio());
        negocioExistente.setCapitalInicial(negocio.getCapitalInicial());
        return negocioRepository.save(negocioExistente);
    }

    @Override
    public void eliminar(Long id) {
        negocioRepository.deleteById(id);
    }

    @Override
    public double calcularUtilidades(Long id) {
        negocioRepository.findById(id).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
        Double ingresos = movimientoRepository.sumMontoByNegocioAndTipo(id, "INGRESO");
        Double egresos = movimientoRepository.sumMontoByNegocioAndTipo(id, "EGRESO");
        Double gastos = movimientoRepository.sumMontoByNegocioAndTipo(id, "GASTO");

        double totalIngresos = ingresos != null ? ingresos : 0.0;
        double totalEgresos = egresos != null ? egresos : 0.0;
        double totalGastos = gastos != null ? gastos : 0.0;

        return totalIngresos - totalEgresos - totalGastos;
    }

    @Override
    public Object verResumenFinanciero(Long idNegocio) {
        negocioRepository.findById(idNegocio).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

        Double ingresos = movimientoRepository.sumMontoByNegocioAndTipo(idNegocio, "INGRESO");
        Double egresos = movimientoRepository.sumMontoByNegocioAndTipo(idNegocio, "EGRESO");
        Double gastos = movimientoRepository.sumMontoByNegocioAndTipo(idNegocio, "GASTO");

        double totalIngresos = ingresos != null ? ingresos : 0.0;
        double totalEgresos = egresos != null ? egresos : 0.0;
        double totalGastos = gastos != null ? gastos : 0.0;

        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalIngreso", totalIngresos);
        resumen.put("totalEgresos", totalEgresos);
        resumen.put("totalGastos", totalGastos);
        resumen.put("utilidad", totalIngresos - totalEgresos - totalGastos);
        return resumen;
    }
}
