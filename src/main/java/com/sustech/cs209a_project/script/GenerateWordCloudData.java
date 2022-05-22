package com.sustech.cs209a_project.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GenerateWordCloudData {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/rawdata/jsonTotalWithLanguage.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            CloudDataItem[] items = gson.fromJson(reader, CloudDataItem[].class);
            LinkedHashSet<Map.Entry<String, Long>> a = Arrays.stream(items).map(CloudDataItem::getTopics).flatMap(Collection::stream)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (Map.Entry<String, Long> i : a) {
                stringBuilder.append("{\"topic\": " + "\"").append(i.getKey()).append("\", \"number\": ").append(i.getValue()).append("},");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
            Files.write(Paths.get("src/main/resources/ripedata/wordCloudData.json"), Collections.singleton(stringBuilder));
            System.out.println();
        }
    }
}

class CloudDataItem {
    String[] topics;

    public List<String> getTopics() {
        if (topics == null) return new ArrayList<>();
        return List.of(topics);
    }
}