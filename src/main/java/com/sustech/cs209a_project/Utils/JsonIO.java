package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class JsonIO {
    public static void main(String[] args) {
        JSONArray json4 = readJSONArray("repos10000to1000000.json");
        JSONArray json1 = readJSONArray("repos10to100.json");
        JSONArray json2 = readJSONArray("repos100to1000.json");
        JSONArray json3 = readJSONArray("repos1000to10000.json");
        System.out.printf("%d, %d, %d, %d\n", json1.size(), json2.size(), json3.size(),json4.size());
        JSONArray jsonTotal = new JSONArray();
        jsonTotal.addAll(json1);
        jsonTotal.addAll(json2);
        jsonTotal.addAll(json3);
        jsonTotal.addAll(json4);
        saveJSONArray(jsonTotal,"jsonTotal.json");

    }

    public static void saveJSONArray(JSONArray jsonArray, String path) {
        saveString(JSON.toJSONString(jsonArray), path);
    }
    public static void saveJSON(JSONObject json, String path) {
        saveString(JSON.toJSONString(json), path);
    }

    public static JSONArray readJSONArray(String path) {
        try (BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String jsonStr = bfr.readLine();
            return JSON.parseArray(jsonStr);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveString(String content, String path) {
        try (BufferedWriter bfw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path)))) {
            bfw.write(content);
            bfw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
