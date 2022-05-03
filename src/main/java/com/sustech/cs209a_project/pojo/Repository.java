package com.sustech.cs209a_project.pojo;

import com.google.gson.annotations.SerializedName;

public class Repository {
        int id;
//    String nodeId;
    String name;
    String fullName;
    //    boolean p;
    GitHubUser owner;
    //    String htmlUrl;
//    String description;
//    String fork;
//    String url;
    String languageUrl;
    String stargazersUrl;
    String[] topics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repository)) return false;

        Repository that = (Repository) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStargazersUrl() {
        return stargazersUrl;
    }
}
