package com.sustech.cs209a_project.service;

import java.io.IOException;

public interface ApiService {
    String getCommitWithTime(String url) throws IOException;

    String getWordCloud(int count) throws IOException;

    String getPopularLicense() throws IOException;

    String getTotalRank() throws IOException;

    String getTotalRankPie() throws IOException;
}
