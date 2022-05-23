package com.sustech.cs209a_project;

import com.sustech.cs209a_project.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Cs209AProjectApplicationTests {

    @Resource
    private ApiService apiService;
    @Test
    void contextLoads() {
        apiService.test();
    }

}
