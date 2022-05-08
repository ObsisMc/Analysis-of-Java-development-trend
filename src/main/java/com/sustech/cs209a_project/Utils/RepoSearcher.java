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
//        String jsonStr = jsonArray.toJSONString();
//        System.out.println(jsonStr);
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
                String qURL = String.format(url, ptr, ptr + step - 1, page);
                Connection.Response response = Jsoup.connect(qURL)
                        .header("Authorization", String.format("token %s", token))
                        .ignoreContentType(true).ignoreHttpErrors(true).execute();
                JSONObject json = JSON.parseObject(response.body());
                if (response.statusCode() == 200) {
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
                page++;
            }
            ptr += step;
        }
        return jsonArrayTotal;
    }
}
