package com.sustech.cs209a_project.script.collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sustech.cs209a_project.pojo.LanguageRank;
import com.sustech.cs209a_project.utils.JsonIO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.*;
import java.util.stream.Collectors;

public class PageCrawler {
    public static List<String> popularLanguage;
    final private static String token = "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";

    public static void getPopularLanguage() {
        JSONArray languages = JsonIO.readJSON("popularLanguage.json").getJSONArray("language");
        popularLanguage = languages.stream().map(o -> (String) o).collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
//        getLanguageNumByYear();
        getLanguageRepoIncrByMonth();
    }

    public static void getJavaInfoFromSearch() {

    }

    public static void getLanguageNumByYear() throws IOException {
        int yearBegin = 2007;
        int yearEnd = 2021;
        Hashtable<Integer, LinkedHashMap<String, Long>> ranks = crawlLanguageIncrByYear(yearBegin, yearEnd);
        // get json
        JSONArray jsonArray = new JSONArray();
        for (int y = yearBegin; y <= yearEnd; y++) {
            System.out.printf("Year %d\n", y);
            String[] languages = new String[10];
            Long[] nums = new Long[10];
            int index = 0;
            for (String lgg : ranks.get(y).keySet()) {
                System.out.printf("%s %d\n", lgg, ranks.get(y).get(lgg));
                languages[index] = lgg;
                nums[index] = ranks.get(y).get(lgg);
                index++;
            }
            LanguageRank languageRank = new LanguageRank(y, languages, nums);
            jsonArray.add(JSON.toJSON(languageRank));
        }
        JsonIO.saveJSONArray(jsonArray, "./rank.json");
    }

    public static Hashtable<Integer, LinkedHashMap<String, Long>> crawlLanguageIncrByYear(int yearBegin, int yearEnd) throws IOException {
        String urlFormat = "https://github.com/search?q=created:%04d";
        Hashtable<Integer, LinkedHashMap<String, Long>> res = new Hashtable<>();
        for (int t = yearBegin; t <= yearEnd; t++) {
            Document document = Jsoup.connect("https://github.com/search?q=created:2015")
                    .header("Authorization", String.format("token %s", token)).get();
            Element applicationMain = document.select("div[class=application-main ]").get(0);
            Element container = applicationMain.selectFirst("div");
            Element sortList = container.select("div").get(1).selectFirst("div").selectFirst("ul");
            Elements languages = sortList.select("li");
            LinkedHashMap<String, Long> rank = new LinkedHashMap<>();
            for (Element e : languages) {
                Element a = e.selectFirst("a");
                Element span = a.selectFirst("span");
                String language = a.ownText();
                String num = span.text();
                rank.put(language, Long.valueOf(num.replaceAll(",", "")));
            }
            res.put(t, rank);
        }
        return res;
    }

    public static void getLanguageRepoIncrByMonth() throws IOException {
        getPopularLanguage();
        Hashtable<String, LinkedHashMap<String, Integer>> res = crawlLanguageRepoIncrByMonth(2007, 2007);
        System.out.println(res);
    }

    public static Hashtable<String, LinkedHashMap<String, Integer>> crawlLanguageRepoIncrByMonth(int yearBegin, int yearEnd) throws IOException {
        String urlFormat = "https://github.com/search?q=created:%s+language:%s&type=repositories";
        Hashtable<String, LinkedHashMap<String, Integer>> res = new Hashtable<>();
        long sleepTime = 16000;
        for (int t = yearBegin; t <= yearEnd; t++) {
            for (int m = 10; m < 11; m++) {
                LinkedHashMap<String, Integer> repoIncr = new LinkedHashMap<>();
                String yearMonth = String.format("%04d-%02d", t, m);
                for (int keyI = 0; keyI < popularLanguage.size(); ) {
                    String key = popularLanguage.get(keyI);
                    try {
                        Document document = Jsoup.connect(String.format(urlFormat, yearMonth, key))
                                .header("Authorization", String.format("token %s", token))
                                .get();
                        Element applicationMain = document.select("div[class=application-main ]").get(0);
                        Element container = applicationMain.select("div[class='container-lg px-md-2 mt-lg-4 clearfix']").get(0);
                        Element repoInfo = container.select("div[class='col-12 col-md-9 float-left px-2 pt-3 pt-md-0 codesearch-results']").get(0);
                        Element px2 = repoInfo.select("div[class='px-2']").get(0);
                        Element h3 = px2.selectFirst("h3");
                        String prompt = h3.text();
                        if (prompt.startsWith("We couldn’t find")) {
                            repoIncr.put(key, 0);
                        } else {
                            repoIncr.put(key, Integer.parseInt(prompt.split(" ")[0].replaceAll(",", "")));
                        }
                    } catch (Exception e) {
                        Connection.Response response = Jsoup.connect(String.format(urlFormat, yearMonth, key))
                                .header("Authorization", String.format("token %s", token)).ignoreHttpErrors(true)
                                .execute();
                        sleepTime = 2 * sleepTime;
                        System.out.printf("Status: %d, url: %s -> begin to sleep:%ds\n",
                                response.statusCode(), response.url(), sleepTime/1000);
                        try {

                            Thread.sleep(sleepTime);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        continue;
                    }
                    keyI++;
                    sleepTime -= 1000;
                    if(sleepTime <0){
                        sleepTime = 16000;
                    }
                }
                res.put(yearMonth, repoIncr);
            }
        }
        return res;
    }
}
