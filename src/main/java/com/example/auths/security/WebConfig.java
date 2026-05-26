package com.example.auths.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from classpath:/adminlte/ at root path
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/adminlte/", "classpath:/static/");

        // Serve uploaded files physically from the uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map root and /login endpoints to resource forwarding
        registry.addViewController("/").setViewName("forward:/auth/login.html");
        registry.addViewController("/login").setViewName("forward:/auth/login.html");
    }
}
