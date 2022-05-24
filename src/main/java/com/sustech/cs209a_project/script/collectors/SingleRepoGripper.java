package com.sustech.cs209a_project.script.collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.cs209a_project.utils.JsonIO;
import com.sustech.cs209a_project.utils.TimeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SingleRepoGripper {
    final private static String token = "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";
    static int n = 0;
    static int per_page = 100;
    static int size;
    static int step;
    static Retinue retinue;
    static JSONObject res = new JSONObject();
    static JSONArray rawArray;
    static float totalNum = 0;
    static int actualSampleN = 0;

    static Lock nLock = new ReentrantLock();
    static Lock sectionLock = new ReentrantLock();

    // CommitWorker HashMap<<String, int[]>>
    static JSONObject commitTime = new JSONObject();


    public static void main(String[] args) {
        taskTwo(400, 10);
    }

    public static void taskOne(int sampleNum, int threadN) {
        rawArray = Objects.requireNonNull(JsonIO.readJSONArray("jsonTotalWithoutDuplicate.json"));
        size = rawArray.size();
        step = (int) Math.ceil((double) size / sampleNum);

        retinue = new Retinue(1);
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

        JsonIO.saveJSON(res, String.format("repoAvgLable_%d.json", sampleNum));
        System.out.printf("Avg: %f\n", totalNum / (float) actualSampleN);
    }

    public static void taskTwo(int sampleNum, int threadN) {
        rawArray = Objects.requireNonNull(JsonIO.readJSONArray("jsonTotalWithLanguage.json"));
        size = rawArray.size();
        step = (int) Math.ceil((double) size / sampleNum);

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadN);
        for (int i = 0; i < threadN; i++) {
            threadPoolExecutor.submit(new CommitWorker());
        }
        threadPoolExecutor.shutdown();
        try {
            boolean finish = threadPoolExecutor.awaitTermination(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            JsonIO.saveJSON(commitTime, String.format("repoCommitTime%d.json", sampleNum));
        }


    }

    static class Retinue {
        static public int taskId;
        static public String task;

        public Retinue(int taskId) {
            Retinue.taskId = taskId;
            switch (taskId) {
                case 0:
                    task = "contributors";
                    break;
                case 1:
                case 2:
                    task = "issues";
                    break;
            }
        }

        public String getURL(String fullName) {
            return "https://api.github.com/repos/" + fullName +
                    "/" + task + "?per_page=" + per_page + "&page=%s";
        }

        public int[] work(JSONArray jsonArray) {
            switch (Retinue.taskId) {
                case 0:
                    return taskOne(jsonArray);
                case 1:
                    return taskTwo(jsonArray);
                case 2:
                    return taskThree(jsonArray);
            }
            return new int[]{0, 0};
        }

        public int[] taskOne(JSONArray jsonArray) {
            return new int[]{jsonArray.size(), 0};
        }

        public int[] taskTwo(JSONArray jsonArray) {
            int num = 0;
            int validN = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                num += object.getInteger("comments");
                validN += 1;
            }
            return new int[]{num, validN};
        }

        public int[] taskThree(JSONArray jsonArray) {
            int num = 0;
            int validN = 0;
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                num += object.getJSONArray("labels").size();
                validN += 1;
            }
            return new int[]{num, validN};
        }

        public float postWork(int validN, int num) {
            switch (Retinue.taskId) {
                case 0:
                    return num;
                case 1:
                case 2: {
                    if (validN != 0)
                        return num / (float) validN;
                    else
                        return 0;
                }

            }
            return 0;
        }
    }

    static class Worker implements Runnable {

        @Override
        public void run() {
            while (n < size) {
                int validN = 0;
                nLock.lock();
                JSONObject repo = rawArray.getJSONObject(n);
                n += step;
                nLock.unlock();

                String fullName = repo.getString("full_name");
                String urlFormat = retinue.getURL(fullName);
                boolean skip = false;
                int pageN = 1;
                int singleN = 0;
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
                                int[] num = retinue.work(JSON.parseArray(response.body()));
                                singleN += num[0];
                                validN += num[1];
                                pageN++;
                                if (num[0] < per_page) {
                                    skip = true;
                                }
                            } catch (Exception | Error e) {
                                System.out.printf("Unknow error at %s\n", url);
                            }
                        } else if (response.statusCode() == 403) {
                            JSONObject rateJson = JSON.parseObject(Jsoup.connect("https://api.github.com/rate_limit")
                                    .header("Authorization", String.format("token %s", token))
                                    .ignoreContentType(true).execute().body());
                            JSONObject searchRate = rateJson.getJSONObject("rate");
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
                        System.out.printf("Exception %s at %s\n", e.getMessage(), url);
                    }
                } while (!skip);
                float tmpN = retinue.postWork(validN, singleN);
                if (tmpN > 0) {
                    sectionLock.lock();
                    res.put(fullName, tmpN);
                    totalNum += tmpN;
                    actualSampleN++;
                    sectionLock.unlock();
                }
            }
        }
    }

    static class CommitWorker implements Runnable {

        @Override
        public void run() {
            while (n < size) {
                nLock.lock();
                JSONObject repo = rawArray.getJSONObject(n);
                n += step;
                nLock.unlock();

                String fullName = repo.getString("full_name");
                String urlFormat = "https://api.github.com/repos/" + fullName + "/commits?per_page=" + per_page + "&page=%s";

                boolean skip = false;
                int pageN = 1;
                int dupicatN = 0;
                do {
                    String url = String.format(urlFormat, pageN);
                    try {
                        Connection.Response response = Jsoup.connect(url)
                                .header("Authorization", String.format("token %s", token))
                                .ignoreContentType(true).ignoreHttpErrors(true)
                                .timeout(10000)
                                .execute();
                        if (response.statusCode() == 200) {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = JSON.parseArray(response.body());
                                assert jsonArray != null;
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    String dateStr = jsonArray.getJSONObject(i).getJSONObject("commit")
                                            .getJSONObject("author").getString("date");
                                    String[] dateStrList = dateStr.split("T");
                                    String date = dateStrList[0];
                                    try {
                                        sectionLock.lock();
                                        if (!commitTime.containsKey(date)) {
                                            JSONArray dayTime = new JSONArray();
                                            for (int dt = 0; dt < 24; dt++) {
                                                JSONObject anHr = new JSONObject();
                                                anHr.put("value", 0);
                                                dayTime.add(anHr);
                                            }
                                            commitTime.put(date, dayTime);
                                        }
                                        int dayHour = Integer.parseInt(dateStrList[1].split(":")[0]);
                                        JSONObject hr = commitTime.getJSONArray(date).getJSONObject(dayHour);
                                        hr.put("value", hr.getInteger("value") + 1);
                                    } finally {
                                        sectionLock.unlock();
                                    }
                                }
                                if (jsonArray.size() < per_page) {
                                    skip = true;
                                }
                                pageN++;
                                dupicatN = -1;
                            } catch (Exception | Error e) {
                                System.out.printf("Unknow error at %s\n", url);
                            }
                        } else if (response.statusCode() == 403) {
                            JSONObject rateJson = JSON.parseObject(Jsoup.connect("https://api.github.com/rate_limit")
                                    .header("Authorization", String.format("token %s", token))
                                    .ignoreContentType(true).execute().body());
                            JSONObject searchRate = rateJson.getJSONObject("rate");
                            if (searchRate.getInteger("remaining") > 0) {
                                System.out.printf("But still have %d remaining\n", searchRate.getInteger("remaining"));
                            }
                            long waitSec = TimeUtils.waitSecond(searchRate.getLong("reset"));
                            if (waitSec > 0) {
                                System.out.printf("Need to wait %ds\n", waitSec);
                                try {
                                    Thread.sleep(waitSec * 1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            System.out.printf("StatusCode %d at %s\n", response.statusCode(), url);
                        }
                    } catch (IOException e) {
                        System.out.printf("Exception %s at %s\n", e.getMessage(), url);
                    }
                    dupicatN++;
                    if (dupicatN > 3) {
                        skip = true;
                    }
                } while (!skip);
            }
        }
    }
}
