package com.sustech.cs209a_project.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sustech.cs209a_project.pojo.CommitSearchResult;
import com.sustech.cs209a_project.pojo.RepositoriesSearchResult;
import com.sustech.cs209a_project.pojo.Repository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RestAPI {

    public static List<Repository> searchJavaRepository(int searchPage) throws IOException {
        if (searchPage > 10) {
            throw new IllegalArgumentException("searchPage must <= 10");
        }
        Set<Repository> result = new HashSet<>();
        for (int i = 1; i <= searchPage; i++) {
            String url = "https://api.github.com/search/repositories?q=language:java&sort=stars&page=" + i + "&per_page=100";
            BufferedReader in = HttpClient.doGet(url);
            Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            RepositoriesSearchResult subResult = gson.fromJson(in, RepositoriesSearchResult.class);
            result.addAll(List.of(subResult.getItems()));
            in.close();
        }
        return new ArrayList<>(result);
    }

    public static void star(Repository repository) {
        String name = repository.getFullName();
        String stargazersUrl = repository.getStargazersUrl();
    }

    public static void getRepositoryStar(String repositoryFullName, String stargazersUrl) throws IOException {
        BufferedReader in = HttpClient.doGet(stargazersUrl);
        Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }


    public static void getCommits(String user, String repo) throws IOException {
        String request = "https://api.github.com/repos/" + user + "/" + repo + "/commits?page=1&per_page=100";
        BufferedReader in = HttpClient.doGet(request);
        Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        CommitSearchResult[] results = gson.fromJson(in, CommitSearchResult[].class);
        System.out.println();
    }

    public static void parseRepo(String user, String repo) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" + user + "/" + repo).get();
        Element application_main = doc.body().getElementsByClass("application-main ").get(0);
        Elements es = application_main.getElementsByClass("d-none d-sm-inline");
        for (Element e : es) {
            var a = e.getElementsByTag("strong");
            if(a.size()==0){
                continue;
            }
            var result = a.get(0).childNode(0);
            System.out.println(result.toString());
        }
        System.out.println();
    }

    public static void main(String[] args) throws IOException {
        parseRepo("xbdeng", "taskManager");
    }
}
