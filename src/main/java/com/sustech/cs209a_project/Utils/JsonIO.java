package com.sustech.cs209a_project.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.cs209a_project.pojo.RelationNode;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonIO {
    public static void main(String[] args) {
        adjustRelationJSON(0.8f);
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

    public static JSONObject readJSON(String path) {
        try (BufferedReader bfr = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String jsonStr = bfr.readLine();
            return JSON.parseObject(jsonStr);
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

    /* followings are specific functions*/
    public void integrateJSON() {
        JSONArray json4 = readJSONArray("repos10000to1000000.json");
        JSONArray json1 = readJSONArray("repos10to100.json");
        JSONArray json2 = readJSONArray("repos100to1000.json");
        JSONArray json3 = readJSONArray("repos1000to10000.json");
        System.out.printf("%d, %d, %d, %d\n", json1.size(), json2.size(), json3.size(), json4.size());
        JSONArray jsonTotal = new JSONArray();
        jsonTotal.addAll(json1);
        jsonTotal.addAll(json2);
        jsonTotal.addAll(json3);
        jsonTotal.addAll(json4);
        saveJSONArray(jsonTotal, "jsonTotal.json");
    }

    public static void adjustRelationJSON(float threshold) {
        String path = "relation55000to60000.json";
        JSONObject json = readJSON(path);
        JSONObject demo = readJSON("demo.json");

        JSONArray jsonLink = json.getJSONArray("links");
        JSONArray jsonNode = json.getJSONArray("nodes");
        JSONArray demoNode = demo.getJSONArray("nodes");
        for (int i = 0; i < jsonNode.size(); i++) {
            JSONObject nodeJSON = jsonNode.getJSONObject(i);
            // delete those too small
            if (nodeJSON.getFloat("value") < threshold) {
                int id = nodeJSON.getInteger("id");
                jsonNode.remove(i--);
                for (int j = 0; j < jsonLink.size(); j++) {
                    JSONObject link = jsonLink.getJSONObject(j);
                    if (link.getInteger("source") == id || link.getInteger("target") == id) {
                        jsonLink.remove(j--);
                    }
                }
                continue;
            }
            RelationNode node = new RelationNode(nodeJSON.getInteger("id"), nodeJSON.getString("name"),
                    nodeJSON.getDouble("symbolSize"), nodeJSON.getDouble("value"), nodeJSON.getFloat("x"),
                    nodeJSON.getFloat("y"), nodeJSON.getInteger("category"));
            if (i < demoNode.size()) {
                float x = demoNode.getJSONObject(i).getFloat("x");
                float y = demoNode.getJSONObject(i).getFloat("y");
                node.x = x;
                node.y = y;
            }
            if (!node.name.equals("Java")) {
                node.symbolSize = 5 * node.symbolSize;
            } else {
                node.symbolSize = 70;
            }
            node.category = parseCategory(node.name);
            jsonNode.set(i, JSONObject.toJSON(node));
        }
        json.put("categories",getCategoryMap());
        saveJSON(json, "adjust_" + path);
    }

    public static int parseCategory(String name) {
        List<String> web = Arrays.asList("CSS", "TypeScript", "JavaScript", "Vue", "HTML");
        List<String> cFamily = Arrays.asList("C", "C++", "C#", "Objective-C");
        List<String> others = Arrays.asList("Clojure", "Scala", "Shell");
        List<String> mobile = Arrays.asList("Groovy", "Kotlin");
        List<String> ai = Arrays.asList("Python");
        ArrayList<String> test;
        if (name.equals("Java")) {
            return 0;
        } else {
            if (web.contains(name)) {
                return 1;
            } else if (cFamily.contains(name)) {
                return 2;
            } else if (mobile.contains(name)) {
                return 3;
            } else if (ai.contains(name)) {
                return 4;
            }
        }

        return 5;
    }

    public static JSONArray getCategoryMap() {
        JSONArray jsonArray = new JSONArray();
        String[] categories = {"Java","Web","C-Family","Mobile","AI&Big Data","Others"};
        for(int i=0;i<categories.length;i++){
            JSONObject tmpObject= new JSONObject();
            tmpObject.put("name",categories[i]);
            jsonArray.add(tmpObject);
        }

        return jsonArray;
    }


}
