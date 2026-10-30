package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.modelo.LogAuditoria;
import com.Proyecto.Gestor_Contable.repository.LogAuditoriaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Service
public class LogAuditoriaServiceImpl {


    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public void registrar(String negocioId, String accion, String detalle) {
        registrar(negocioId, accion, detalle, obtenerIp());
    }

    public void registrar(String negocioId, String accion, String detalle, String ip) {
        try {
            LogAuditoria log = new LogAuditoria();
            log.setNegocioId(negocioId);
            log.setAccion(accion);
            log.setDetalle(detalle);
            log.setFecha(LocalDateTime.now());
            log.setIp(ip);
            logAuditoriaRepository.save(log);
        } catch (Exception e) {
            // La auditoría nunca debe romper la operación principal
        }
    }

    private String obtenerIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return null;
            HttpServletRequest request = attrs.getRequest();
            String forwarded = request.getHeader("X-Forwarded-For");
            if (forwarded != null && !forwarded.isBlank()) {
                return forwarded.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            return null;
        }
    }
}
