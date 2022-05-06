package com.sustech.cs209a_project.pojo;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class LanguageRank {
    public int year;
    public String[] language;
    public Long[] nums;

    public LanguageRank(int year, String[] language, Long[] nums){
        this.year = year;
        this.language = language;
        this.nums = nums;
    }
}
