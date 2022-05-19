package com.sustech.cs209a_project.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sustech.cs209a_project.pojo.RelationNode;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonIO {
    public static void main(String[] args) {
//        adjustRelationJSON(0.8f);
//        getAllTopTopic();
//        transToVisFormat();
        checkDuplication(true);
//        test();
    }

    public static void test() {
        JSONArray jsonArray = readJSONArray("jsonTotal.json");
        String date_json1 = jsonArray.getJSONObject(0).getString("updated_at");
        String date_json2 = jsonArray.getJSONObject(1).getString("updated_at");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            Date date1 = df.parse(date_json1);
            Date date2 = df.parse(date_json2);
            System.out.printf("%s > %s: %d", date1, date2, date1.compareTo(date2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void checkDuplication(boolean drop) {
//        String jsonPath = "jsonTotal.json";
        String jsonPath = "jsonTotal.json";
        String outputPath = "jsonTotalWithoutDuplicate.json";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        JSONArray jsonArray = readJSONArray(jsonPath);
        assert jsonArray != null;
        HashSet<Integer> ids = new HashSet<>();
        HashMap<Integer, JSONObject> res = new HashMap<>();

        int duplicateN = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject tmp = jsonArray.getJSONObject(i);
            int id = tmp.getInteger("id");
            if (ids.contains(id)) {
                duplicateN++;

                try {
                    Date now_date = df.parse(tmp.getString("updated_at"));
                    Date before_date = df.parse(res.get(id).getString("updated_at"));
                    if (now_date.compareTo(before_date) >= 0) {
                        res.put(id, tmp);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                ids.add(id);
                res.put(id, tmp);
            }
        }
        if(drop){
            JSONArray resArray = new JSONArray();
            for(Integer id: res.keySet()){
                resArray.add(res.get(id));
            }
            saveJSONArray(resArray,outputPath);
        }
        System.out.printf("Actual size: %d, duplication: %d", ids.size(), duplicateN);

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
        json.put("categories", getCategoryMap());
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
        String[] categories = {"Java", "Web", "C-Family", "Mobile", "AI&Big Data", "Others"};
        for (int i = 0; i < categories.length; i++) {
            JSONObject tmpObject = new JSONObject();
            tmpObject.put("name", categories[i]);
            jsonArray.add(tmpObject);
        }

        return jsonArray;
    }

    public static void getAllTopTopic() {
        int[] star = {0, 100, 1000000};
        LinkedHashMap[] topics = new LinkedHashMap[star.length - 1];
        for (int i = 1; i < star.length; i++) {
            topics[i - 1] = getTopTopics(star[i - 1], star[i]);
            String wordsFrequencyStr = JSON.toJSONString(topics[i - 1]);
            saveString(wordsFrequencyStr, String.format("wordsFrequency%dkTO%dk.json", star[i - 1] / 1000, star[i] / 1000));
            System.out.printf("Finish %d\n", star[i]);
        }

        String wordsFrequencyStr = JSON.toJSONString(getTopTopics(0, 100000000));
        saveString(wordsFrequencyStr, String.format("wordsFrequency.json"));
        System.out.printf("Finish\n");


    }

    public static LinkedHashMap<String, Integer> getTopTopics(int starBegin, int starEnd) {
        String jsonPath = "./jsonTotal.json";
        String stoperPath = "stop_words.txt";
        JSONArray json = readJSONArray(jsonPath);
        assert json != null;

        HashMap<String, Integer> wordsFrequency = new HashMap<>();
        ArrayList<String> stops = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stoperPath)))) {
            String tmp;
            while ((tmp = br.readLine()) != null) {
                stops.add(tmp.toLowerCase(Locale.ROOT));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < json.size(); i++) {
            JSONObject repo = json.getJSONObject(i);
            int star = repo.getInteger("stargazers_count");
            if (!(star >= starBegin && star < starEnd)) {
                continue;
            }
            JSONArray topics = repo.getJSONArray("topics");
            for (int j = 0; j < topics.size(); j++) {
                String word = topics.getString(j);
                if (!stops.contains(word)) {
                    if (wordsFrequency.containsKey(word)) {
                        wordsFrequency.put(word, wordsFrequency.get(word) + 1);
                    } else {
                        wordsFrequency.put(word, 1);
                    }
                }
            }
        }
        List<Map.Entry<String, Integer>> topTopics = new ArrayList<>(wordsFrequency.entrySet());
        Collections.sort(topTopics, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        LinkedHashMap<String, Integer> topicsMap = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            topicsMap.put(topTopics.get(i).getKey(), topTopics.get(i).getValue());
        }
        return topicsMap;
    }

    public static void transToVisFormat() {
        JSONObject json = readJSON("wordsFrequency0kTO0k.json");
        assert json != null;

        JSONArray res = new JSONArray();
        for (String key : json.keySet()) {
            JSONObject word = new JSONObject();
            word.put("number", json.getInteger(key));
            word.put("topic", key);
            res.add(word);
        }
        saveJSONArray(res, "wordCloudVis.json");
    }

}
