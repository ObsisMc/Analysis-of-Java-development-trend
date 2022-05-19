package com.sustech.cs209a_project.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UncheckedIOException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageSearcher {
    public static List<String> popularLanguage;
    final private static String token = "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";

    public static void main(String[] args) throws IOException {
        getLanguageRepoIncrByMonth();
    }

    public static void getPopularLanguage() {
        JSONArray languages = JsonIO.readJSON("popularLanguage.json").getJSONArray("language");
        popularLanguage = languages.stream().map(o -> (String) o).collect(Collectors.toList());
    }

    public static void getLanguageRepoIncrByMonth() throws IOException {
        getPopularLanguage();
        String[] searchItems = {"repositories", "users", "issues"};
        String[] searchJsonName = {"Repo", "User", "Issue"};
        for (int i = 1; i < searchItems.length; i++) {
            JSONObject res = crawlLanguageRepoIncrByMonth(2007, 2021, searchItems[i]);
            JsonIO.saveJSON(res, "language" + searchJsonName[i] + "ByTime.json");
        }

    }

    public static String parseURLName(String name) {
        return name.replaceAll("#", "%23").replaceAll("[+]", "%2B");
    }

    public static JSONObject crawlLanguageRepoIncrByMonth(int yearBegin, int yearEnd, String type) throws IOException {
        assert type.equals("repositories") || type.equals("users") || type.equals("issues");

        String urlFormat = "https://api.github.com/search/" + type + "?q=language:%s+created:%s";
        String keyName = "total_count";
        JSONObject res = new JSONObject();
        for (int y = yearBegin; y <= yearEnd; y++) {
            for (int m = 1; m < 2; m++) {
                JSONArray repoIncr = new JSONArray();
                String yearMonth = String.format("%04d", y);
                for (int keyI = 0; keyI < popularLanguage.size(); ) {
                    String languageKey = popularLanguage.get(keyI);
                    try {
                        Connection.Response response = Jsoup.connect(String.format(urlFormat, parseURLName(languageKey), yearMonth))
                                .header("Authorization", String.format("token %s", token))
                                .ignoreContentType(true).ignoreHttpErrors(true)
                                .timeout(5000)
                                .execute();
                        String bodyStr = null;
                        try {
                            bodyStr = response.body();
                        } catch (Exception | Error e) {
                            System.out.println("Jsoup has unknown error!!");
                            continue;
                        }

                        if (response.statusCode() == 200) {
                            JSONObject resJson = JSON.parseObject(bodyStr);
                            int count = resJson.getInteger(keyName);
                            JSONObject languageEntry = new JSONObject();
                            languageEntry.put("name", languageKey);
                            languageEntry.put("count", count);
                            repoIncr.add(languageEntry);
                            keyI++;
                        } else if (response.statusCode() == 403) {
                            System.out.println(JSON.parseObject(bodyStr).getString("message"));
                            JSONObject rateJson = JSON.parseObject(Jsoup.connect("https://api.github.com/rate_limit")
                                    .header("Authorization", String.format("token %s", token))
                                    .ignoreContentType(true).execute().body());
                            JSONObject searchRate = rateJson.getJSONObject("resources")
                                    .getJSONObject("search");
                            if (searchRate.getInteger("remaining") > 0) {
                                System.out.printf("But still have %d remaining\n", searchRate.getInteger("remaining"));
                                continue;
                            }
                            long waitSec = TimeUtils.waitSecond(searchRate.getLong("reset"));
                            if (waitSec <= 0) {
                                continue;
                            }
                            System.out.printf("Need to wait %ds\n", waitSec);
                            try {
                                Thread.sleep(waitSec * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else if (response.statusCode() == 404) {
                            System.out.printf("Not found at %s\n", response.url());
                        } else {


                        }
                    } catch (SocketTimeoutException e) {
                        System.out.printf("Timeout at %s, %s\n", languageKey, yearMonth);
                    }
                }
                res.put(yearMonth, repoIncr);
                if (m == 6) {
                    System.out.printf("Finish half months of year %d\n", y);
                }
            }
            System.out.printf("-->Finish year %d\n", y);
        }
        return res;
    }
}
