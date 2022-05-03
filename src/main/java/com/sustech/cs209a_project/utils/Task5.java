package com.sustech.cs209a_project.utils;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task5 {
    static String url;
    static int lowerYear;
    static int[][][] nums;
    static int THREADN = 4;
    static final private Lock numsLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        int timeSpan = 1;
        int monthN = 4;
        int dayN = 1;
        getCreatedNumByTime(timeSpan, monthN, dayN);

    }

    /**
     * get num of created repo from [now year - timespan, now year - 1]
     *
     * @param timeSpan year span
     * @throws InterruptedException nothing
     *
     */
    static public void getCreatedNumByTime(int timeSpan, int monthN, int dayN) throws  InterruptedException {

        nums = new int[timeSpan][monthN][dayN];
        lowerYear = Calendar.getInstance().get(Calendar.YEAR) - timeSpan;
        url = "https://api.github.com/search/repositories?q=language:java+created:%04d-%02d-%02d";
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADN);
        int mSpan = monthN / THREADN;
        for (int i = 0; i < THREADN; i++) {
            threadPoolExecutor.submit(new Worker(timeSpan, i * mSpan, (i + 1) * mSpan, dayN));
        }
        threadPoolExecutor.shutdown();
        boolean finish = threadPoolExecutor.awaitTermination(1, TimeUnit.HOURS);

        for (int i = 0; i < timeSpan; i++) {
            for (int j = 0; j < monthN; j++) {
                for (int k = 0; k < dayN; k++) {
                    System.out.println(nums[i][j][k]);
                }
            }
        }
    }

    static class Worker implements Runnable {
        public int timeSpan;
        public int lowerm;
        public int upperm;
        public int dayN;

        public Worker(int t, int lm, int um, int d) {
            this.timeSpan = t;
            this.lowerm = lm;
            this.upperm = um;
            this.dayN = d;
        }

        @Override
        public void run() {
            for (int yindex = 0; yindex < timeSpan; yindex++) {
                int y = lowerYear + yindex;
                for (int mindex = lowerm; mindex < upperm; mindex++) {
                    int m = mindex + 1;
                    for (int dindex = 0; dindex < dayN; dindex++) {
                        int d = dindex + 1;
                        int status;
                        JSONObject jsonObject;

                        String queryURL = String.format(url, y, m, d);
                        Connection.Response response;
                        try {
                            response = Jsoup.connect(queryURL)
                                    .ignoreContentType(true).ignoreHttpErrors(true).execute();
                            status = response.statusCode();
                            jsonObject = JSONObject.parseObject(response.body());

                            if (status == 200) {
                                numsLock.lock();
                                nums[yindex][mindex][dindex] += jsonObject.getIntValue("total_count");
                                numsLock.unlock();
                            } else {
                                System.out.printf("Status:%d. %s\n", status, jsonObject.getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.printf("PID %d Finish %04d-%02d\n", Thread.currentThread().getId(), y, m);
                }
                System.out.printf("PID %d Finish %04d\n", Thread.currentThread().getId(), y);
            }

        }
    }

}
