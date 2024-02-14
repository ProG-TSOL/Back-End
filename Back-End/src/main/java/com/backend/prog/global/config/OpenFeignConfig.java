package com.backend.prog.global.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.backend.prog")
public class OpenFeignConfig {
}
