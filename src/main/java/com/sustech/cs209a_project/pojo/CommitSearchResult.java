package com.sustech.cs209a_project.pojo;

public class CommitSearchResult {
    Commit commit;

    public String getCommitTime(){
        return commit.author.date.substring(0,10);
    }
}


class Commit{
    Author author;
}

class Author{
    String name;
    String email;
    String date;
}