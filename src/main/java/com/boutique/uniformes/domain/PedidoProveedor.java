package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos_proveedor")
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El número de pedido es obligatorio")
    @Column(name = "numero_pedido", unique = true, nullable = false)
    private String numeroPedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @NotNull(message = "La fecha del pedido es obligatoria")
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;

    @NotNull(message = "La fecha de entrega es obligatoria")
    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "prioridad")
    private String prioridad = "media"; // baja, media, alta, urgente

    @Column(name = "condiciones_pago", columnDefinition = "TEXT")
    private String condicionesPago;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "impuesto_porcentaje", precision = 5, scale = 2)
    private BigDecimal impuestoPorcentaje = BigDecimal.ZERO;

    @Column(name = "impuesto_monto", precision = 10, scale = 2)
    private BigDecimal impuestoMonto = BigDecimal.ZERO;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "estado")
    private String estado = "PENDIENTE"; // PENDIENTE, ENVIADO, COMPLETADO, CANCELADO

    @OneToMany(mappedBy = "pedidoProveedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetallePedidoProveedor> detalles;

    @Column(name = "usuario_creador")
    private String usuarioCreador;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) { this.proveedor = proveedor; }

    public LocalDate getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDate fechaPedido) { this.fechaPedido = fechaPedido; }

    public LocalDate getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDate fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public String getCondicionesPago() { return condicionesPago; }
    public void setCondicionesPago(String condicionesPago) { this.condicionesPago = condicionesPago; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getImpuestoPorcentaje() { return impuestoPorcentaje; }
    public void setImpuestoPorcentaje(BigDecimal impuestoPorcentaje) { this.impuestoPorcentaje = impuestoPorcentaje; }

    public BigDecimal getImpuestoMonto() { return impuestoMonto; }
    public void setImpuestoMonto(BigDecimal impuestoMonto) { this.impuestoMonto = impuestoMonto; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetallePedidoProveedor> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedidoProveedor> detalles) { this.detalles = detalles; }

    public String getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(String usuarioCreador) { this.usuarioCreador = usuarioCreador; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
