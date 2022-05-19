package com.sustech.cs209a_project.pojo;

public class CommitSearchResult {
    Commit commit;
}


class Commit{
    Author author;
}

class Author{
    String name;
    String email;
    String date;
}