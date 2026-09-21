package com.Proyecto.Gestor_Contable.modelo;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "analisis_financieros_ia")
public class AnalisisFinancieroIA {
    @Id
    private String id;
    private String negocioId;
    private String periodo;
    private String textoGenerado;
    private String tipoAlerta;
    private LocalDateTime fechaGeneracion;
}
