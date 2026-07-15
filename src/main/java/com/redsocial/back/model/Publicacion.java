package com.redsocial.back.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa una publicación en la red social.
 * Corresponde a la tabla {@code publicaciones} de la base de datos.
 */
@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion {

    /** Identificador único de la publicación */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Contenido textual de la publicación */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    /** Fecha y hora de publicación (generada automáticamente por la base de datos) */
    @Column(name = "fecha_publicacion", insertable = false, updatable = false)
    private LocalDateTime fechaPublicacion;

    /** Usuario autor de la publicación */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
