package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido_proveedor")
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoProveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_proveedor_id")
    private PedidoProveedor pedidoProveedor;

    @NotBlank(message = "La descripción es obligatoria")
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a 0")
    @Column(name = "precio_unitario", precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    // Referencia opcional a uniforme existente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniforme_id")
    private Uniforme uniforme;

    @Column(name = "codigo_uniforme")
    private String codigoUniforme;

    @PrePersist
    @PreUpdate
    public void calculateTotal() {
        if (cantidad != null && precioUnitario != null) {
            this.total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoProveedor getPedidoProveedor() { return pedidoProveedor; }
    public void setPedidoProveedor(PedidoProveedor pedidoProveedor) { this.pedidoProveedor = pedidoProveedor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { 
        this.cantidad = cantidad; 
        calculateTotal();
    }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { 
        this.precioUnitario = precioUnitario; 
        calculateTotal();
    }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Uniforme getUniforme() { return uniforme; }
    public void setUniforme(Uniforme uniforme) { this.uniforme = uniforme; }

    public String getCodigoUniforme() { return codigoUniforme; }
    public void setCodigoUniforme(String codigoUniforme) { this.codigoUniforme = codigoUniforme; }
}
