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

    //[
    //              { value: 40, name: 'rose 1' },
    //              { value: 38, name: 'rose 2' },
    //              { value: 32, name: 'rose 3' },
    //              { value: 30, name: 'rose 4' },
    //              { value: 28, name: 'rose 5' },
    //              { value: 26, name: 'rose 6' },
    //              { value: 22, name: 'rose 7' },
    //              { value: 18, name: 'rose 8' }
    //            ]
    @GetMapping("popular_license")
    public void getPopularLicense(){

    }


}
