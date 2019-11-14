package com.cpren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@EnableCaching
public class AiKnowledgeApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(AiKnowledgeApplication.class, args);
    }

}
