package com.redsocial.back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Red Social Backend.
 * Inicia el contexto de Spring Boot con todas sus configuraciones automáticas.
 */
@SpringBootApplication
public class BackApplication {

	/**
	 * Punto de entrada de la aplicación.
	 *
	 * @param args argumentos de línea de comandos (no requeridos)
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackApplication.class, args);
	}

}
