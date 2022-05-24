package com.sustech.cs209a_project.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommitSearchResult {
    Commit commit;

    public String getCommitTime(){
        return commit.author.date.substring(0,10);
    }

    public String getCommitWeekHour(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar cal = Calendar.getInstance();
        Date date;
        String datetime = getCommitTime();
        try {
            date = sdf.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String weekDay = weekDays[w];
        String hour = commit.author.date.substring(11,13);
        return weekDay + hour;
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