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


    /**
     * [{date:"2021-09-17",count:"8"},{date:"2021-09-19",count:"6"},{date:"2021-09-26",count:"7"},...]
     */
    @GetMapping("commit_times")
    public String getCommitTimes(String url) {
        try {
            return apiService.getCommitWithTime(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {
     * "head": [
     * "C#",
     * "Java",
     * "C++",
     * "CSS",
     * "C",
     * "Perl",
     * "HTML",
     * "CoffeeScript",
     * "JavaScript",
     * "PHP",
     * "Ruby",
     * "Dart",
     * "Python"
     * ],
     * "dataset": [
     * {
     * "year": 2007,
     * "language": "Ruby",
     * "value": 1
     * },
     * {
     * "year": 2007,
     * "language": "C",
     * "value": 0
     * },
     * {
     * "year": 2007,
     * "language": "C#",
     * "value": 0
     * },....
     * ]
     */
    @GetMapping("total_rank")
    public void getTotalRank() {
        try {
            apiService.getTotalRank();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * [{"year":2007, "language": [{"name":"Ruby", "value":1},{"name":"C", "value":0},{"name":"C#", "value":0},{"name":"C++", "value":0}...]},....]
     */
    @GetMapping("total_rank_pie")
    public void getTotalRankPie() {
        try {
            apiService.getTotalRankPie();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("increase_rank")
    public void getIncreaseRank() {
        try {
            apiService.getIncreaseRank();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // [{"topic": "ababa", number: 1}]
    @GetMapping("word_cloud")
    public String getWordCloud(int number) {
        try {
            return apiService.getWordCloud(number);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public String getPopularLicense() {
        try {
            return apiService.getPopularLicense();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {
     * repos:["top1RepoName","top2RepoName","top3RepoName"],
     * data:{
     * stars:[1,2,3],
     * forks:[],
     * watchs:[]
     * }
     * }
     */
    @GetMapping("top_repo_rank")
    public void getTopRepoRank() {

    }

    @GetMapping("language_relation")
    public void getLanguageRelation() {

    }


}
