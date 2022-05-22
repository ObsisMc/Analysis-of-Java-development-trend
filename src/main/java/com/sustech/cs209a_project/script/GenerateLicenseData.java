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

public class GenerateLicenseData {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/rawdata/jsonTotalWithLanguage.json");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            LicenseItem[] licenseItems = gson.fromJson(reader, LicenseItem[].class);
            Map<String, Long> sortedMap = new LinkedHashMap<>();
            Arrays.stream(licenseItems).map(LicenseItem::getKey)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream().sorted((o1, o2) -> (int) (o2.getValue() - o1.getValue()))
                    .forEachOrdered(i -> sortedMap.put(i.getKey(), i.getValue()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("[");
            for (Map.Entry<String, Long> i : sortedMap.entrySet()) {
                if (i.getKey().equals("")) {
                    continue;
                }
                stringBuilder.append("{\"name\": " + "\"").append(i.getKey()).append("\", \"value\": ").append(i.getValue()).append("},");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
            Files.write(Paths.get("src/main/resources/ripedata/licenseData.json"), Collections.singleton(stringBuilder));
            System.out.println();
        }
    }
}

class LicenseItem {
    License license;

    public String getKey() {
        if (license == null) {
            return "";
        }
        return license.key;
    }
}

class License {
    String key;
}