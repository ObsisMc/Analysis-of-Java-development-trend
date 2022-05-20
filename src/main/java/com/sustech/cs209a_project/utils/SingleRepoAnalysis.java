package com.sustech.cs209a_project.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleRepoAnalysis {
    final private static String token = "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";
    static int n = 0;
    static int size;
    static int step;
    static JSONObject res = new JSONObject();
    static JSONArray rawArray;
    static int totalNum = 0;
    static int actualSampleN = 0;
    static Lock nLock = new ReentrantLock();
    static Lock sectionLock = new ReentrantLock();


    public static void main(String[] args) {
        taskOne(1500, 10);
    }

    public static void taskOne(int sampleNum, int threadN) {
        rawArray = Objects.requireNonNull(JsonIO.readJSONArray("jsonTotalWithoutDuplicate.json"));
        size = rawArray.size();
        step = (int) Math.ceil((double) size / sampleNum);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadN);
        for (int i = 0; i < threadN; i++) {
            threadPoolExecutor.submit(new Worker());
        }
        threadPoolExecutor.shutdown();
        try {
            boolean finish = threadPoolExecutor.awaitTermination(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JsonIO.saveJSON(res, "repoContributorNum.json");
        System.out.printf("Avg: %f\n", totalNum / (float) actualSampleN);
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (n < size) {
                nLock.lock();
                JSONObject repo = rawArray.getJSONObject(n);
                n += step;
                nLock.unlock();

                String fullName = repo.getString("full_name");
                String urlFormat = "https://api.github.com/repos/" + fullName +
                        "/contributors?per_page=100&page=%s";
                boolean skip = false;
                int pageN = 1;
                int contributorN = 0;
                do {
                    String url = String.format(urlFormat, pageN);
                    try {
                        Connection.Response response = Jsoup.connect(url)
                                .header("Authorization", String.format("token %s", token))
                                .ignoreContentType(true).ignoreHttpErrors(true)
                                .timeout(5000)
                                .execute();

                        if (response.statusCode() == 200) {
                            try {
                                JSONArray jsonArray = JSON.parseArray(response.body());
                                if (jsonArray.size() > 0) {
                                    contributorN += jsonArray.size();
                                    pageN++;
                                } else {
                                    skip = true;
                                }
                            } catch (Exception | Error e) {
                                System.out.printf("Unknow error at %s", url);
                            }
                        } else if (response.statusCode() == 403) {
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
                        } else {
                            skip = true;
                        }

                    } catch (IOException e) {
                        System.out.printf("Timeout at %s\n", url);
                    }
                } while (!skip);
                if (contributorN > 0) {
                    sectionLock.lock();
                    res.put(fullName, contributorN);
                    totalNum += contributorN;
                    actualSampleN++;
                    sectionLock.unlock();
                }
            }
        }
    }
}
