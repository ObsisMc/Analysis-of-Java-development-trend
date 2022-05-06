package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.sustech.cs209a_project.pojo.LanguageRank;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;

public class Crawler {
    public static void main(String[] args) throws IOException {
        int yearBegin = 2021;
        int yearEnd = 2021;
        Hashtable<Integer, LinkedHashMap<String, Long>> ranks = getLanguageNumByYear(yearBegin, yearEnd);
        for (int y : ranks.keySet()) {
            System.out.printf("Year %d\n", y);
            for (String lgg : ranks.get(y).keySet()) {
                System.out.printf("%s %d\n", lgg, ranks.get(y).get(lgg));
            }
        }
        HashMap<String, Long> test = new HashMap<>();
        test.put("test",1L);
        LanguageRank languageRank = new LanguageRank(2021, test);
        String rankJsonStr = JSON.toJSONString(languageRank);
        System.out.println(rankJsonStr);
    }

    public static Hashtable<Integer, LinkedHashMap<String, Long>> getLanguageNumByYear(int yearBegin, int yearEnd) throws IOException {
        String urlFormat = "https://github.com/search?q=created:%04d";
        Hashtable<Integer, LinkedHashMap<String, Long>> res = new Hashtable<>();
        for (int t = yearBegin; t <= yearEnd; t++) {
            Document document = Jsoup.connect(String.format(urlFormat, 2021)).get();
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
                rank.put(language, Long.valueOf(num.replaceAll(",","")));
            }
            res.put(t, rank);
        }
        return res;
    }
}
