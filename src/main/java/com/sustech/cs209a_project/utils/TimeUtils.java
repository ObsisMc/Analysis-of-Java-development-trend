package com.sustech.cs209a_project.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static void main(String[] args) {
        System.out.println(waitSecond(1652978987));
    }
    public static long waitSecond(long epochSecond){
        Instant epochSec = Instant.ofEpochSecond(epochSecond);
        ZoneId zId = ZoneId.systemDefault();
        ZonedDateTime then = ZonedDateTime.ofInstant(epochSec, zId);
        ZonedDateTime now = ZonedDateTime.now();
        return ChronoUnit.SECONDS.between(now, then);
    }
    public static ZonedDateTime getDate(long epochSecond){
        Instant epochSec = Instant.ofEpochSecond(epochSecond);
        ZoneId zId = ZoneId.systemDefault();
        ZonedDateTime then = ZonedDateTime.ofInstant(epochSec, zId);
        return then;
    }
}
