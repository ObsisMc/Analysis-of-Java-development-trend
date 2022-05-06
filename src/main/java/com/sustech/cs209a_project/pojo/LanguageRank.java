package com.sustech.cs209a_project.pojo;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class LanguageRank {
    public int year;
    LinkedHashMap<String, Long> rank;
    HashMap<String, Long> ranktest;

    public LanguageRank(int year, LinkedHashMap<String, Long> rank){
        this.year = year;
        this.rank = rank;
    }

    public LanguageRank(int year, HashMap<String, Long> rank){
        this.year = year;
        this.ranktest = rank;
    }
}
