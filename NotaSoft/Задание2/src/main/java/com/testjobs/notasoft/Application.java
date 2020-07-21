package com.testjobs.notasoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.testjobs.notasoft.repositories")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }
}
