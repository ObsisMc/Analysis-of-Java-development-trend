package com.sustech.cs209a_project.script;

import com.google.gson.Gson;
import com.sustech.cs209a_project.pojo.Language;
import com.sustech.cs209a_project.pojo.TotalRankItem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateThreeData {
    public static void main(String[] args) throws IOException {
        try (FileInputStream issueInputStream = new FileInputStream("src/main/resources/rawdata/LanguageTotalIssueByTime.json");
             FileInputStream repoInputStream = new FileInputStream("src/main/resources/rawdata/LanguageTotalRepoByTime.json");
             FileInputStream userInputStream = new FileInputStream("src/main/resources/rawdata/LanguageTotalUserByTime.json");
             Reader issueReader = new InputStreamReader(issueInputStream, StandardCharsets.UTF_8);
             Reader repoReader = new InputStreamReader(repoInputStream, StandardCharsets.UTF_8);
             Reader userReader = new InputStreamReader(userInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            List<ResultDataSet> resultDataSets = new ArrayList<>();

            TotalRankItem[] issueRankItems = gson.fromJson(issueReader, TotalRankItem[].class);
            List<ResultData> issueResultData = new ArrayList<>();
            for (TotalRankItem item : issueRankItems) {
                int year = item.year;
                List<Language> languages = Arrays.stream(item.language).sorted((o1, o2) -> o2.value - o1.value).limit(7).collect(Collectors.toList());
                issueResultData.add(new ResultData(year, languages));
            }
            resultDataSets.add(new ResultDataSet("issue", issueResultData));

            TotalRankItem[] userRankItems = gson.fromJson(userReader, TotalRankItem[].class);
            List<ResultData> userResultData = new ArrayList<>();
            for (TotalRankItem item : userRankItems) {
                int year = item.year;
                List<Language> languages = Arrays.stream(item.language).sorted((o1, o2) -> o2.value - o1.value).limit(7).collect(Collectors.toList());
                userResultData.add(new ResultData(year, languages));
            }
            resultDataSets.add(new ResultDataSet("user", userResultData));

            TotalRankItem[] repoRankItems = gson.fromJson(repoReader, TotalRankItem[].class);
            List<ResultData> repoResultData = new ArrayList<>();
            for (TotalRankItem item : repoRankItems) {
                int year = item.year;
                List<Language> languages = Arrays.stream(item.language).sorted((o1, o2) -> o2.value - o1.value).limit(7).collect(Collectors.toList());
                repoResultData.add(new ResultData(year, languages));
            }
            resultDataSets.add(new ResultDataSet("repo", repoResultData));

            Files.write(Paths.get("src/main/resources/ripedata/threeData.json"), Collections.singleton(gson.toJson(resultDataSets)));

        }
    }
}

class ResultDataSet {
    String type;
    ResultData[] dataset;

    public ResultDataSet(String type, List<ResultData> resultData) {
        this.type = type;
        this.dataset = resultData.toArray(new ResultData[0]);
    }
}

class ResultData {
    int year;
    Language[] languages;

    public ResultData(int year, List<Language> languages) {
        this.year = year;
        this.languages = languages.toArray(new Language[0]);
    }
}