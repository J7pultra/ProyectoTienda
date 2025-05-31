package com.boutique.uniformes.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Column(nullable = false)
    private String apellido;
    
    @NotBlank(message = "El documento es obligatorio")
    @Column(unique = true, nullable = false)
    private String documento;
    
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Formato de teléfono inválido")
    private String telefono;
    
    private String direccion;
    
    private String ciudad;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    
    @Column(name = "activo")
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Venta> ventas;
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
