package com.example.demo.config;

import com.example.demo.servlet.BasicServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletRegistrationBean<BasicServlet> basicServletRegistration() {
        ServletRegistrationBean<BasicServlet> registration = new ServletRegistrationBean<>(new BasicServlet());
        registration.addUrlMappings("/basic");
        return registration;
    }
}