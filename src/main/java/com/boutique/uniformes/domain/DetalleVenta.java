package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La venta es obligatoria")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @NotNull(message = "El uniforme es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniforme_id", nullable = false)
    private Uniforme uniforme;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser positiva")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calcularSubtotal();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        calcularSubtotal();
    }

    // Anotaciones removidas, solo método utilitario
    private void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // Agregar estos métodos:
    public Uniforme getUniforme() {
        return this.uniforme;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return this.precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

}
