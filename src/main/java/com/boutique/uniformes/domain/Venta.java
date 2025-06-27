package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroFactura;

    @NotNull(message = "El cliente es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "El vendedor es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Empleado vendedor;

    @Column(name = "fecha_venta")
    private LocalDateTime fechaVenta = LocalDateTime.now();

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "descuento", precision = 10, scale = 2)
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "impuesto", precision = 10, scale = 2)
    private BigDecimal impuesto = BigDecimal.ZERO;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private EstadoVenta estado = EstadoVenta.COMPLETADA;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detalles;

    public enum EstadoVenta {
        PENDIENTE, COMPLETADA, CANCELADA
    }

    @PrePersist
    private void generarNumeroFactura() {
        if (numeroFactura == null) {
            numeroFactura = "FAC-" + System.currentTimeMillis();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Agregar estos métodos:
    public List<DetalleVenta> getDetalles() {
        return this.detalles;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public BigDecimal getImpuesto() {
        return this.impuesto;
    }

    public BigDecimal getDescuento() {
        return this.descuento;
    }

    public EstadoVenta getEstado() {
        return this.estado;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setImpuesto(BigDecimal impuesto) {
        this.impuesto = impuesto;
    }

    public void setEstado(EstadoVenta estado) {
        this.estado = estado;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public LocalDateTime getFechaVenta() {
        return this.fechaVenta;
    }

}
