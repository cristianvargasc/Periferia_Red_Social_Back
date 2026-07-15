package com.redsocial.back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario registrado en la red social.
 * Corresponde a la tabla {@code usuarios} de la base de datos.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    /** Identificador único del usuario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de usuario único para autenticación */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /** Contraseña del usuario */
    @Column(nullable = false, length = 255)
    private String password;

    /** Nombres del usuario */
    @Column(nullable = false, length = 100)
    private String nombres;

    /** Apellidos del usuario */
    @Column(nullable = false, length = 100)
    private String apellidos;

    /** Fecha de nacimiento del usuario */
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /** Alias público único del usuario en la red social */
    @Column(unique = true, nullable = false, length = 50)
    private String alias;

    /** Fecha y hora de creación del registro (gestionada por la base de datos) */
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}
