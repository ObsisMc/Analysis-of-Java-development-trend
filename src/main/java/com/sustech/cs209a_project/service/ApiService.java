package com.sustech.cs209a_project.service;

import java.io.IOException;

public interface ApiService {
    String getCommitWithTime(String url,String identity) throws IOException;

    String getWordCloud(int count) throws IOException;

    String getPopularLicense() throws IOException;

    String getTotalRank() throws IOException;

    String getTotalRankPie() throws IOException;

    String getIncreaseRank() throws IOException;

    String getUserIssueRepo() throws IOException;

    void auth(String code, String identity);

    String getTopRepoRank() throws IOException;
}
