package com.sustech.cs209a_project.script;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.cs209a_project.utils.JsonIO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GenerateWorkTimeData {
    public static void main(String[] args) {
        JSONObject repoCommitTime = JsonIO.readJSON("src/main/resources/rawdata/repoCommitTime400.json");
        DataParser dataParser = new DataParser();

//        int y = dataParser.parseStrDate("2022-5-22");
//        System.out.println(y);

        JSONObject commitTime = new JSONObject();
        JSONArray hrs = new JSONArray();
        for (int i = 0; i < 24; i++) {
            hrs.add(String.format("%dh", i));
        }
        commitTime.put("hours", hrs);

        JSONArray days = new JSONArray();
        String[] weekList = dataParser.getWeekList();
        for (int i = 0; i < 7; i++) {
            days.add(weekList[i]);
        }
        commitTime.put("days", days);

        JSONArray data = new JSONArray();
        int[][] hrM = new int[7][24];
        assert repoCommitTime != null;
        for (String key : repoCommitTime.keySet()) {
            int y = dataParser.parseStrDate(key);
            for (int i = 0; i < 24; i++) {
                hrM[y][i] += repoCommitTime.getJSONArray(key).getJSONObject(i).getInteger("value");
            }
        }
        for (int i = 0; i < 24 * 7; i++) {
            JSONArray node = new JSONArray();
            int y = i / 24;
            int x = i % 24;
            node.add(y);
            node.add(x);
            node.add(hrM[y][x]);
            data.add(node);
        }
        commitTime.put("data", data);
        JsonIO.saveJSON(commitTime,"commitTime.json");
    }
}

class DataParser {
    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
    String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thr", "Fri", "Sat"};
    Calendar calendar = Calendar.getInstance();

    public int parseStrDate(String dataStr) {
        Date date = null;
        try {
            date = f.parse(dataStr);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int wek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (wek < 0)
            wek = 0;
        return wek;
    }

    public String[] getWeekList() {
        return this.weekDays;
    }
}
