package com.Proyecto.Gestor_Contable.service.lmpl;

import com.Proyecto.Gestor_Contable.modelo.LogAuditoria;
import com.Proyecto.Gestor_Contable.repository.LogAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogAuditoriaServiceImpl {

    @Autowired
    private LogAuditoriaRepository logAuditoriaRepository;

    public  void registrar (String usuarioId, String accion, String detalle, String ip){
        LogAuditoria log = new LogAuditoria();
        log.setUsuarioId(usuarioId);
        log.setAccion(accion);
        log.setDetalle(detalle);
        log.setFecha(LocalDateTime.now());
        log.setIp(ip);

        logAuditoriaRepository.save(log);

    }
}
