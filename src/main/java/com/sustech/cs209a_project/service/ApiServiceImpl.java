package com.sustech.cs209a_project.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sustech.cs209a_project.pojo.CommitSearchResult;
import com.sustech.cs209a_project.utils.HttpClient;
import com.sustech.cs209a_project.utils.PublicUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl implements ApiService {
    private int getCommitCount(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element application_main = doc.body().getElementsByClass("application-main ").get(0);
        Elements es = application_main.getElementsByClass("d-none d-sm-inline");
        for (Element e : es) {
            Elements a = e.getElementsByTag("strong");
            if (a.size() == 0) {
                continue;
            }
            Node result = a.get(0).childNode(0);
            String t = result.toString().replaceAll(",","");
            return Integer.parseInt(t);
        }
        throw new RuntimeException("Server error");
    }

    @Override
    public String getCommitWithTime(String url) throws IOException {
        int count = getCommitCount(url);
        String repo = PublicUtils.getUserAndRepoFromUrl(url);
        int iterationTime = count / 100 + 1;
        List<CommitSearchResult> searchResults = new ArrayList<>();
        for (int i = 1; i <= iterationTime; i++) {
            System.out.println("hello" + i + " begin");
            String request = "https://api.github.com/repos/" + repo + "/commits?page=" + i + "&per_page=100";
            BufferedReader in = HttpClient.doGet(request);
            Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            CommitSearchResult[] result = gson.fromJson(in, CommitSearchResult[].class);
            searchResults.addAll(List.of(result));
            System.out.println("hello" + i + " end");
        }
        Map<String, Long> s = searchResults.stream().map(CommitSearchResult::getCommitTime).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Gson gson = new Gson();
        String a = gson.toJson(s);
        System.out.println(a);
        return a;
    }

//    public static void main(String[] args) throws IOException {
//        ApiServiceImpl apiService = new ApiServiceImpl();
//        apiService.getCommitWithTime("https://github.com/xbdeng/taskManager");
//    }
}
