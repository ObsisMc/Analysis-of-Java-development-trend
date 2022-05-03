package com.sustech.cs209a_project.Utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sustech.cs209a_project.pojo.RepositoriesSearchResult;
import com.sustech.cs209a_project.pojo.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static void main(String[] args) throws IOException {
        List<Repository> repositories = searchJavaRepository(1);
    }
}
