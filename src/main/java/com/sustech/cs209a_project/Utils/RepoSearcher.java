package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

public class RepoSearcher {
    static String token = "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";

    public static void main(String[] args) throws IOException, InterruptedException {
        JSONArray jsonArray = getReposByStar(120000, 130000);
        JSONArray storedJson = new JSONArray();
        storedJson.addAll(jsonArray);
        JsonIO.saveJSONArray(storedJson,"./repos.json");
        String jsonStr = jsonArray.toJSONString();
        System.out.println(jsonStr);
    }

    public static JSONArray getReposByStar(int starLow, int starUp) throws IOException, InterruptedException {
        String api = "https://api.github.com/search/repositories";
        String query = "?sort=stars&q=language:java+stars:%d..%d&per_page=100&page=%d";
        String url = api + query;
        int maxPage = 1000 / 100;

        int step = 10000;
        int ptr = starLow;
        JSONArray jsonArrayTotal = new JSONArray();
        while (ptr < starUp) {
            int page = 1;
            while (page <= maxPage) {
                String qURL = String.format(url, ptr, ptr + step - 1, page);
                Connection.Response response = Jsoup.connect(qURL)
                        .header("Authorization", String.format("token %s", token))
                        .ignoreContentType(true).ignoreHttpErrors(true).execute();
                JSONObject json = JSON.parseObject(response.body());
                if (response.statusCode() == 200) {
                    JSONArray jsonArray = json.getJSONArray("items");
                    if (jsonArray.size() != 0) {
                        jsonArrayTotal.addAll(jsonArray);
                    }else{
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
                page++;
            }

            ptr += step;
        }
        return jsonArrayTotal;
    }
}
