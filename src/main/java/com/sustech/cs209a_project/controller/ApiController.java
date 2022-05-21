package com.sustech.cs209a_project.controller;


import com.sustech.cs209a_project.service.ApiService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiController {

    @Resource
    private ApiService apiService;

    @GetMapping("commit_times")
    public String getCommitTimes(String url) {
        try {
            return apiService.getCommitWithTime(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("total_rank")
    public void getTotalRank(){

    }


    @GetMapping("total_rank_pie")
    public void getTotalRankPie(){

    }

    @GetMapping("increase_rank")
    public void getIncreaseRank(){

    }


    // [{"topic": "ababa", number: 1}]
    @GetMapping("world_cloud")
    public void getWorldCloud(int number){

    }


    @GetMapping("popular_license")
    public void getPopularLicense(){

    }


    @GetMapping("top_repo_rank")
    public void getTopRepoRank(){

    }


}
