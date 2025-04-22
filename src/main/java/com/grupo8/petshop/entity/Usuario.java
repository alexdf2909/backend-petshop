package com.grupo8.petshop.entity;

import com.grupo8.petshop.util.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = true)
    private String codigoVerificacion;

    @Column(nullable = false)
    private Boolean verificado = false;

    @Column(nullable = true)
    private String refreshToken;

    @Column(nullable = true)
    private Date refreshTokenExpiry;

    @ManyToMany
    @JoinTable(
            name = "usuario_favorito",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private Set<Producto> favoritos = new HashSet<>();

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isDeleted;
}
