package com.estapar.garage.garage_management.aplication.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("/**")
            .allowedOrigins("http://localhost:3003")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }
}