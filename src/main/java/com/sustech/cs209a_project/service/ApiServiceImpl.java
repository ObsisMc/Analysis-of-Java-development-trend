package com.sustech.cs209a_project.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sustech.cs209a_project.pojo.*;
import com.sustech.cs209a_project.utils.PublicUtils;
import com.sustech.cs209a_project.utils.RedisUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiServiceImpl implements ApiService {

    @Resource
    RedisUtil redisUtil;

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
    public String getCommitWithTime(String url, String identity) throws IOException {
        String access_token = (String) redisUtil.get(identity);
        if (redisUtil.hasKey("[commitWithTime]" + url)) {
            redisUtil.expire("[commitWithTime]" + url, 10 * 60);
            return (String) redisUtil.get("[commitWithTime]" + url);
        }
        int count = getCommitCount(url);
        String repo = PublicUtils.getUserAndRepoFromUrl(url);
        int iterationTime = count / 100 + 1;
        List<CommitSearchResult> searchResults = new ArrayList<>();
        for (int i = 1; i <= iterationTime; i++) {
            System.out.println("hello" + i + " begin");
            String request = "https://api.github.com/repos/" + repo + "/commits?page=" + i + "&per_page=100";
            HttpGet httpGet = new HttpGet(request);
            httpGet.setHeader("Authorization", "token " + access_token);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String r = EntityUtils.toString(response.getEntity());
            Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            CommitSearchResult[] result = gson.fromJson(r, CommitSearchResult[].class);
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
        redisUtil.set("[commitWithTime]" + url, stringBuilder.toString(), 10 * 60);
        return stringBuilder.toString();
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

    @Override
    public String getTotalRankPie() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/rankPieData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }


    @Override
    public String getIncreaseRank() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/increaseRankData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }

    @Override
    public String getUserIssueRepo() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/ripedata/threeData.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        }
    }


    @Override
    public void auth(String code, String identity) {
        String accessTokenUrl = "https://github.com/login/oauth/access_token?client_id=1b620213701eebcda787&client_secret=37e216c3aafc2e4fa6f79c111fed46618d270c20&code=" + code;
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        RestTemplate request = new RestTemplate();
        String s = request.postForObject(accessTokenUrl, String.class, String.class, map);
        String[] str = s.split("&");
        String access_token = str[0].substring(str[0].indexOf("=")).substring(1);
        redisUtil.set(identity, access_token, 60 * 30);
    }


    @Override
    public String getTopRepoRank() throws IOException {
        HttpGet httpGet = new HttpGet("https://api.github.com/search/repositories?q=language:java+stars%3A%3E10000&sort=stars&page=1&per_page=3");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpGet);
        String r = EntityUtils.toString(response.getEntity());
        Gson gson = new GsonBuilder().setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        RepositoriesSearchResult repositoriesSearchResult = gson.fromJson(r, RepositoriesSearchResult.class);
        Repository[] repositories = repositoriesSearchResult.getItems();
        return gson.toJson(repositories,Repository[].class);
    }


}
