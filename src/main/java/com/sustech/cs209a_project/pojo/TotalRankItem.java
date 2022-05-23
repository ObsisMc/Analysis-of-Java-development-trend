package com.sustech.cs209a_project.pojo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TotalRankItem {

    public int year;
    public Language[] language;

    public List<String> getLanguages() {
        return Arrays.stream(language).map(i -> i.name).collect(Collectors.toList());
    }
}
