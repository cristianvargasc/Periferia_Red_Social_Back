package com.redsocial.back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa un "like" dado por un usuario a una publicación.
 * Corresponde a la tabla {@code likes} de la base de datos.
 */
@Entity
@Table(name = "likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    /** Identificador único del like */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario que dio el like */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Publicación que recibió el like */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    /** Fecha y hora en que se registró el like (gestionada por la base de datos) */
    @Column(name = "fecha_like", insertable = false, updatable = false)
    private LocalDateTime fechaLike;
}
