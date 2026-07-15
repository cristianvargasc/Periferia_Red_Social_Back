package com.redsocial.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de redirección a la documentación Swagger UI.
 * Permite acceder a la interfaz de documentación de la API desde la ruta /docs.
 */
@Controller
public class DocsRedirectController {

    /**
     * Redirige la solicitud a la interfaz Swagger UI.
     *
     * @return redirección a swagger-ui/index.html
     */
    @GetMapping("/docs")
    public String redirigirASwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
