package com.example.bolalarakademiyasi.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StorageProperties storageProperties;


//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedOrigins("http://localhost:5173", "http://127.0.0.1:5173")
//                .allowedMethods("*")
//                .allowedHeaders("*");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Path.of(storageProperties.root()).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(location.endsWith("/") ? location : location + "/");
    }
}
