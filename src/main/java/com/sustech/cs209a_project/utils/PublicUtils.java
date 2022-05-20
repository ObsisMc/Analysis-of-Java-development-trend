package com.sustech.cs209a_project.utils;


public class PublicUtils {
    public static String getUserAndRepoFromUrl(String url){
        return url.substring(19);
    }

}
