package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.cs209a_project.pojo.RelationNode;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.SQLSyntaxErrorException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class RepoSearcher {
    static String token = "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";

    public static void main(String[] args) throws IOException, InterruptedException {
//        getRepoStarTenToMAX();
        getRelationship();

    }

    /**
     * main function for get json of repos whose stars are from 10 to maximum
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public static void getRepoStarTenToMAX() throws IOException, InterruptedException {
        int[][] starBound = {{10, 40, 70, 100, 200, 400, 1000, 2000, 7000, 10000, 1000000},
                {-1, 1, 5, 10, 10, 50, 100, 0, 0, 0, 0}};
        JSONArray storedJson = new JSONArray();
        for (int i = 1; i < starBound[0].length; i++) {
            JSONArray jsonArray = getReposByStar(starBound[0][i - 1], starBound[0][i], starBound[1][i]);
            storedJson.addAll(jsonArray);
            System.out.printf("Finish a section %d to %d: size->%d\n", starBound[0][i - 1], starBound[0][i], jsonArray.size());
            if (i == 3 || i == 6 || i == 9 || i == 10) {
                JsonIO.saveJSONArray(storedJson, String.format("./repos%dto%d.json",
                        i == 10 ? 10000 : starBound[0][i] / 10, starBound[0][i]));
                System.out.printf("->Finish %d: size is %d\n", starBound[0][i], storedJson.size());
                storedJson = new JSONArray();
            }
        }
    }

    public static void getRelationship() throws IOException {
        JSONArray jsonArray = JsonIO.readJSONArray("jsonTotal.json");
        assert jsonArray != null;
        System.out.printf("Number of repos: %d\n", jsonArray.size());

        double step = 5000;
        int times = (int) Math.ceil(jsonArray.size() / step);
        for (int i = 10; i < times; i++) {
            int begin = (int) step * i;
            int end = (int) step * (i + 1);
            System.out.printf("Begin %d ~ %d\n", begin, end);
            JSONObject json = getRelationWithOthersFromJSONArray(jsonArray, begin, end);
            JsonIO.saveJSON(json, String.format("relation%dto%d.json", begin, end));
        }

    }

    public static JSONObject getRelationWithOthersFromJSONArray(JSONArray jsonArray, int begin, int end) {
        String urlKey = "languages_url";
        double scale = 0.0001;

        // get raw data
        ArrayList<String> languageName = new ArrayList<>();
        ArrayList<Double> nums = new ArrayList<>();
        HashMap<String, HashSet<String>> rawLinks = new HashMap<>();

        for (int i = begin; i < Math.min(end, jsonArray.size()); i++) {
            JSONObject test = jsonArray.getJSONObject(i);
            String url = jsonArray.getJSONObject(i).getString(urlKey);

            // invoke api
            boolean hasGet = false;
            while (!hasGet) {
                Connection.Response response = null;
                try {
                    response = Jsoup.connect(url)
                            .header("Authorization", String.format("token %s", token))
                            .ignoreHttpErrors(true).ignoreContentType(true).execute();
                } catch (IOException e) {
                    System.out.println("Trigger exception when get language_url:  " + e.getMessage());
                    continue;
                }
                JSONObject jsonRes = JSON.parseObject(response.body());
                if (response.statusCode() == 200) {
                    hasGet = true;
                    double sum = 0;
                    HashMap<String, Double> tmp = new HashMap<>();
                    for (String key : jsonRes.keySet()) {
                        double num = jsonRes.getDouble(key);
                        tmp.put(key, num);
                        sum += num;
                    }
                    // calc percent value of languages
                    for (Iterator<String> it = tmp.keySet().iterator(); it.hasNext(); ) {
                        String key = it.next();
                        double ratio = tmp.get(key) / sum;
                        if (ratio < 0.001) {
                            it.remove();
                            continue;
                        }
                        if (languageName.contains(key)) {
                            int index = languageName.indexOf(key);
                            nums.set(index, nums.get(index) + ratio);
                        } else {
                            languageName.add(key);
                            nums.add(ratio);
                        }
                        if (key.equals("Java")) {
                            it.remove();
                        }
                    }
                    // get links of other languages
                    for (String key : tmp.keySet()) {
                        for (String key2 : tmp.keySet()) {
                            if (key2.equals(key)) {
                                continue;
                            }
                            if (!(rawLinks.containsKey(key) || rawLinks.containsKey(key2))) {
                                rawLinks.put(key, new HashSet<>() {
                                    {
                                        add(key2);
                                    }
                                });
                            } else {
                                if (rawLinks.containsKey(key)) {
                                    rawLinks.get(key).add(key2);
                                } else {
                                    rawLinks.get(key2).add(key);
                                }
                            }
                        }
                    }

                } else if (response.statusCode() == 404) {
                    System.out.println("Not found at " + url);
                    hasGet = true;
                } else {
                    // rate is limited by github
                    JSONObject rateJson = null;
                    try {
                        rateJson = JSON.parseObject(Jsoup.connect("https://api.github.com/rate_limit")
                                .header("Authorization", String.format("token %s", token))
                                .ignoreContentType(true).execute().body());
                    } catch (IOException e) {
                        System.out.println("Trigger exception when get rate_limit: " + e.getMessage());
                        continue;
                    }
                    if (rateJson.getJSONObject("rate").getInteger("remaining") > 0) {
                        System.out.println("Fault at " + url);
                        System.out.println(jsonRes.getString("message"));
                    } else {
                        Instant epochSec = Instant.ofEpochSecond(rateJson.getJSONObject("rate").getLong("reset"));
                        ZoneId zId = ZoneId.systemDefault();
                        ZonedDateTime then = ZonedDateTime.ofInstant(epochSec, zId);
                        ZonedDateTime now = ZonedDateTime.now();
                        long diffMin = ChronoUnit.MINUTES.between(now, then);
                        long diffSec = ChronoUnit.SECONDS.between(now, then);
                        System.out.printf("Need to wait %d min\n (now %s)", diffMin, now);
                        try {
                            Thread.sleep(diffSec * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (i % 1000 == 0) {
                System.out.printf("Finish %d\n", i);
            }
        }

        // transfer to json with a certain format
        JSONObject json = new JSONObject();
        JSONArray nodes = new JSONArray();
        JSONArray links = new JSONArray();
        JSONArray categories = new JSONArray();

        // init nodes and links with java
        int javaId = languageName.indexOf("Java");
        for (int i = 0; i < languageName.size(); i++) {
            RelationNode relationNode = new RelationNode(i, languageName.get(i), nums.get(i), nums.get(i),
                    i * 10, 0, 0);
            nodes.add(JSON.toJSON(relationNode));
            if (javaId != i) {
                JSONObject link = new JSONObject();
                link.put("source", javaId);
                link.put("target", i);
                links.add(link);
            }

        }
        // init links
        for (String key : rawLinks.keySet()) {
            int keyId = languageName.indexOf(key);
            for (String v : rawLinks.get(key)) {
                JSONObject link = new JSONObject();
                link.put("source", keyId);
                link.put("target", languageName.indexOf(v));
                links.add(link);
            }
        }
        // init categories
        JSONObject category = new JSONObject();
        category.put("name", "Language");
        categories.add(category);

        // integrate
        json.put("nodes", nodes);
        json.put("links", links);
        json.put("categories", categories);
        return json;
    }


    public static JSONArray getReposByStar(int starLow, int starUp, int step) throws IOException, InterruptedException {
        String api = "https://api.github.com/search/repositories";
        String query = "?sort=stars&q=language:java+stars:%d..%d&per_page=100&page=%d";
        String url = api + query;
        int maxPage = 1000 / 100;

        step = step > 0 ? step : starUp - starLow;
        int ptr = starLow;
        JSONArray jsonArrayTotal = new JSONArray();
        while (ptr < starUp) {
            int page = 1;
            while (page <= maxPage) {
                boolean hasGet = false;
                String qURL = String.format(url, ptr, ptr + step - 1, page);
                while (!hasGet) {
                    Connection.Response response = Jsoup.connect(qURL)
                            .header("Authorization", String.format("token %s", token))
                            .ignoreContentType(true).ignoreHttpErrors(true).execute();
                    JSONObject json = JSON.parseObject(response.body());
                    if (response.statusCode() == 200) {
                        hasGet = true;
                        JSONArray jsonArray = json.getJSONArray("items");
                        if (jsonArray.size() != 0) {
                            jsonArrayTotal.addAll(jsonArray);
                        } else {
                            break;
                        }
                    } else {
                        System.out.println(json.getString("message"));
                        JSONObject rateJson = JSON.parseObject(Jsoup.connect("https://api.github.com/rate_limit")
                                .header("Authorization", String.format("token %s", token))
                                .ignoreContentType(true).execute().body());
                        System.out.println(rateJson.getJSONObject("resources").getString("search"));
                        Thread.sleep(5000);
                    }
                }
                page++;
            }
            ptr += step;
        }
        return jsonArrayTotal;
    }
}
