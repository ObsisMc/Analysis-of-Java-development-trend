package com.sustech.cs209a_project.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sustech.cs209a_project.pojo.CommitSearchResult;
import com.sustech.cs209a_project.pojo.LicenseItem;
import com.sustech.cs209a_project.pojo.WordItem;
import com.sustech.cs209a_project.utils.HttpClient;
import com.sustech.cs209a_project.utils.PublicUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
            String t = result.toString().replaceAll(",", "");
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
        LinkedHashSet<Map.Entry<String, Long>> s = searchResults.stream().map(CommitSearchResult::getCommitTime).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toCollection(LinkedHashSet::new));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (Map.Entry<String, Long> t : s) {
            stringBuilder.append("{date:\"").append(t.getKey()).append("\",count:\"").append(t.getValue()).append("\"},");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        System.out.println(stringBuilder);

        return stringBuilder.toString();
    }


    public void getTotalRanks() {

    }

    @Override
    public String getWordCloud(int count) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/wordCloudData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            WordItem[] items = gson.fromJson(reader, WordItem[].class);
            List<WordItem> itemList = List.of(items);
            itemList = itemList.subList(0, count);
            return gson.toJson(itemList);
        }
    }


    @Override
    public String getPopularLicense() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/licenseData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            LicenseItem[] items = gson.fromJson(reader, LicenseItem[].class);
            return gson.toJson(items);
        }
    }

    @Override
    public String getTotalRank() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/totalRankData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }

//    public static void main(String[] args) throws IOException {
//        ApiServiceImpl apiService = new ApiServiceImpl();
//        apiService.getCommitWithTime("https://github.com/xbdeng/taskManager");
//        System.out.println(apiService.getTotalRank());
//    }
}
