package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.sql.SQLOutput;


public class StarGeter {
    final static String baseURL = "https://api.codetabs.com/v1/stars/?repo=01%s ";

    public static void main(String[] args) {
        JSONObject repoStarHistory = getManyRepoStar();

        String outputPath = "starHistory.json";
        JsonIO.saveJSON(repoStarHistory, outputPath);
    }

    public static JSONObject getManyRepoStar() {
        JSONObject res = new JSONObject();
        int starSSLow = 10000;
        int starSSUp = 100000000;

        String repos = "jsonTotalWithoutDuplicate.json";
        JSONArray reposJson = JsonIO.readJSONArray(repos);
        assert reposJson != null;
        int n = 0;
        for (int i = 0; i < reposJson.size(); i++) {
            JSONObject repo = reposJson.getJSONObject(i);
            int stars = repo.getInteger("stargazers_count");
            if (!(stars >= starSSLow && stars < starSSUp)) {
                continue;
            }
            String fullName = repo.getString("full_name");
            JSONArray starHistory = getRepoStar(fullName);
            res.put(fullName, starHistory);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            n++;
            if (n == 100) {
                System.out.printf("Finish %d", n);
            }
        }
        return res;
    }

    public static JSONArray getRepoStar(String fullName) {
        JSONArray res = new JSONArray();
        String URL = String.format(baseURL, fullName);
        try {
            Connection.Response response = Jsoup.connect(URL)
                    .ignoreContentType(true)
                    .ignoreHttpErrors(true).execute();
            String body = response.body();
            JSONArray jsonRes = null;
            try {
                jsonRes = JSON.parseArray(body);
            } catch (Exception e) {

            }

            int statusCode = response.statusCode();
            if (statusCode == 200) {
                res = jsonRes;
            } else if (statusCode == 429) {
                Thread.sleep(4500);
            } else if (statusCode == 400) {
                System.out.printf("%s doesn't exist.", fullName);
            } else {
                System.out.printf("Unknown Error: %s", fullName);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return res;
    }
}
