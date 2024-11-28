package com.projetjee.projetjeespringboot;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/"); // Chemin où se trouvent vos JSP
        resolver.setSuffix(".jsp");           // Extension des fichiers
        resolver.setOrder(1);                 // Priorité du résolveur (si plusieurs)
        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configuration pour servir des fichiers statiques (CSS, JS, etc.)
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }
}



