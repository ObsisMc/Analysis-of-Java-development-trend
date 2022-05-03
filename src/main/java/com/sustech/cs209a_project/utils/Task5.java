package com.sustech.cs209a_project.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class Task5 {
    public static void main(String[] args) throws IOException {
        int timeSpan = 1;
        int monthN = 1;
        int dayN = 1;
        int[][][] createdNum = getCreatedNumByTime(timeSpan,monthN,dayN);

        for(int i=0;i<timeSpan;i++){
            for(int j=0;j<monthN;j++){
                for(int k=0;k<dayN;k++){
                    System.out.println(createdNum[i][j][k]);
                }
            }
        }

    }

    /**
     * get num of created repo from [now year - timespan, now year - 1]
     *
     * @param timeSpan year span
     * @return 3d array containing numbers
     * @throws IOException
     */
    static public int[][][] getCreatedNumByTime(int timeSpan, int monthN, int dayN) throws IOException {

        int[][][] nums = new int[timeSpan][monthN][dayN];
        int lowerYear = Calendar.getInstance().get(Calendar.YEAR) - timeSpan;
        String url = "https://api.github.com/search/repositories?q=language:java+created:%04d-%02d-%02d";
        for (int yindex = 0; yindex < timeSpan; yindex++) {
            int y = lowerYear + yindex;
            for (int mindex = 0; mindex < monthN; mindex++) {
                int m = mindex + 1;
                for (int dindex = 0; dindex < dayN; dindex++) {
                    int d = dindex + 1;
                    int status = 200;
                    JSONObject jsonObject = null;

                    String queryURL = String.format(url, y, m, d);
                    Connection.Response response = Jsoup.connect(queryURL)
                            .ignoreContentType(true).ignoreHttpErrors(true).execute();
                    status = response.statusCode();
                    if (status == 200) {
                        jsonObject = JSONObject.parseObject(response.body());
                        nums[yindex][mindex][dindex] += jsonObject.getIntValue("total_count");
                    }

                }
                System.out.printf("Finish %04d-%02d\n", y, m);
            }
            System.out.printf("Finish %04d\n", y);
        }
        return nums;
    }

}
