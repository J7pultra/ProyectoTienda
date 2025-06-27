package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "asistencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "El empleado es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;
    
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    
    @Column(name = "hora_entrada")
    private LocalTime horaEntrada;
    
    @Column(name = "hora_salida")
    private LocalTime horaSalida;
    
    @Enumerated(EnumType.STRING)
    private EstadoAsistencia estado = EstadoAsistencia.PRESENTE;
    
    private String observaciones;
    
    public enum EstadoAsistencia {
        PRESENTE, AUSENTE, TARDANZA, PERMISO
    }
    
    // Agregar estos métodos:
public LocalDate getFecha() {
    return this.fecha;
}

public void setFecha(LocalDate fecha) {
    this.fecha = fecha;
}

public Empleado getEmpleado() {
    return this.empleado;
}

public void setEmpleado(Empleado empleado) {
    this.empleado = empleado;
}

public LocalTime getHoraSalida() {
    return this.horaSalida;
}

public void setHoraSalida(LocalTime horaSalida) {
    this.horaSalida = horaSalida;
}

public void setHoraEntrada(LocalTime horaEntrada) {
    this.horaEntrada = horaEntrada;
}

public void setEstado(EstadoAsistencia estado) {
    this.estado = estado;
}

}
