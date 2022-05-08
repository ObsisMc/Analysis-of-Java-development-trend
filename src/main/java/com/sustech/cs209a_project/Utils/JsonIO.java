package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class JsonIO {
    public static void saveJSONArray(JSONArray jsonArray, String path) {
        saveString(JSON.toJSONString(jsonArray), path);
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
