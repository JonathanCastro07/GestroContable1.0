package com.Proyecto.Gestor_Contable.modelo;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Document(collection = "logs_auditoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogAuditoria {

    @Id
    private String id;
    private String usuarioId;
    private String accion;
    private String detalle;
    private LocalDateTime fecha;
    private String ip;
}
