package com.project.sbt.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secrets.env")
public class AppConfig {
//
    public AppConfig() {
        System.out.println("Loaded secrets.env: " + System.getenv("SENDGRID_API_KEY"));
    }
}