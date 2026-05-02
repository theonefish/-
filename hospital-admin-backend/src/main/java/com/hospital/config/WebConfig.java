package com.hospital.config;

import com.hospital.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 认证相关
                        "/auth/login", "/auth/wx-login", "/auth/wx-register",
                        // 公开查询接口（游客可访问）
                        "/department/list", "/department/detail/**",
                        "/doctor/list", "/doctor/detail/**",
                        "/news/list", "/news/detail/**",
                        "/schedule/list",
                        "/notice/list", "/notice/detail/**",
                        // 其他
                        "/upload/**", "/error", "/swagger-ui/**", "/v3/api-docs/**"
                );
    }
}
