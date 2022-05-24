package com.sustech.cs209a_project.pojo;

public class IssueSearchResult {
    int comments;

    public boolean isCommit(){
        return comments > 0;
    }
}


