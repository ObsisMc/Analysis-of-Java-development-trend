package com.sustech.cs209a_project.pojo;

import com.google.gson.annotations.SerializedName;

public class RepositoriesSearchResult {
    int totalCount;

    boolean incompleteResults;

    Repository[] items;

    public Repository[] getItems() {
        return items;
    }
}
