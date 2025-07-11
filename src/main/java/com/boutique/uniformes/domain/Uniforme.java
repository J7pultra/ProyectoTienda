package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "uniformes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uniforme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Column(unique = true, nullable = false)
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotBlank(message = "La talla es obligatoria")
    private String talla;

    private String color;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Column(name = "stock_actual")
    private Integer stockActual;

    @Column(name = "stock_minimo")
    private Integer stockMinimo = 5;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "activo")
    private Boolean activo = true;

    @OneToMany(mappedBy = "uniforme", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleVenta> detallesVenta;

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
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isBajoStock() {
        return stockActual <= stockMinimo;
    }

    // Agregar estos métodos:
    public Long getId() {
        return this.id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public BigDecimal getPrecio() {
        return this.precio;
    }

    public Integer getStockActual() {
        return this.stockActual;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

}
