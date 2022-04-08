package com.sustech.cs209a_project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sustech.cs209a_project.mapper")
public class Cs209AProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cs209AProjectApplication.class, args);
    }

}
