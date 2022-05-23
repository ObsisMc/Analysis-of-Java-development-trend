package com.sustech.cs209a_project.script;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateTotalRankData {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("src/main/resources/rawdata/LanguageTotalRepoByTime.json");
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder rankPieBuild = new StringBuilder();
        rankPieBuild.append("[");
        try (Reader reader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().create();
            TotalRankItem[] totalRankItems = gson.fromJson(reader, TotalRankItem[].class);
            List<Language> languages;

            stringBuilder.append("], \"dataset\":[");
            Set<String> showLanguages = new HashSet<>();
            for (TotalRankItem item : totalRankItems) {
                rankPieBuild.append("{\"year\":" + item.year + ", \"language\": [");
                languages = Arrays.stream(item.language).sorted((o1, o2) -> o2.value - o1.value).limit(7).collect(Collectors.toList());
                for (Language language : languages) {
                    rankPieBuild.append("{\"name\":" + "\"" + language.name + "\", \"value\":" + language.value + "},");
                }
                rankPieBuild.deleteCharAt(rankPieBuild.length() - 1);
                rankPieBuild.append("]},");
                showLanguages.addAll(languages.stream().map(i -> i.name).collect(Collectors.toList()));
                for (Language language : languages) {
                    stringBuilder.append("{\"year\":").append(item.year).append(", \"language\": \"").append(language.name).append("\", \"value\": ").append(language.value).append("},");
                }
            }

            rankPieBuild.append("]");
            Files.write(Paths.get("src/main/resources/ripedata/rankPieData.json"), Collections.singleton(rankPieBuild));
            StringBuilder stringBuilder0 = new StringBuilder();
            stringBuilder0.append("{\"head\":[");
            for (String s : showLanguages) {
                stringBuilder0.append("\"").append(s).append("\",");
            }
            stringBuilder0.deleteCharAt(stringBuilder0.length() - 1);
            stringBuilder0.append(stringBuilder);

            stringBuilder0.deleteCharAt(stringBuilder0.length() - 1);
            stringBuilder0.append("]}");
            Files.write(Paths.get("src/main/resources/ripedata/totalRankData.json"), Collections.singleton(stringBuilder0));
            System.out.println();
        }
    }
}

class TotalRankItem {
    int year;
    Language[] language;

    public List<String> getLanguages() {
        return Arrays.stream(language).map(i -> i.name).collect(Collectors.toList());
    }

}

class Language {
    String name;
    int value;
}