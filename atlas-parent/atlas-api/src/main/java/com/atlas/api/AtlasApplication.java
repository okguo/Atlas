package com.atlas.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.atlas")
public class AtlasApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }
}
