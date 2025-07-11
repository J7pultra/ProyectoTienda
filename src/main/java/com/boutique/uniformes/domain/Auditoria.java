package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditorias")
@Data
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String entidad;
    private String operacion;
    private String usuario;
    private LocalDateTime fecha;
    private String detalle;
}
