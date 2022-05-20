package com.sustech.cs209a_project.controller;


import com.sustech.cs209a_project.service.ApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiController {

    @Resource
    private ApiService apiService;

    @GetMapping("commit_times")
    public void getCommitTimes(String url) {
        System.out.println(url);
    }


}
