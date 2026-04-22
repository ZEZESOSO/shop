package com.example.shop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /images/** 로 시작하는 경로로 요청이 들어오면 uploadPath(실제 로컬 경로)에서 파일을 찾음
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}