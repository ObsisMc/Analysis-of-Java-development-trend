package com.sustech.cs209a_project.Utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {
    public static long waitSecond(long epochSecond){
        Instant epochSec = Instant.ofEpochSecond(epochSecond);
        ZoneId zId = ZoneId.systemDefault();
        ZonedDateTime then = ZonedDateTime.ofInstant(epochSec, zId);
        ZonedDateTime now = ZonedDateTime.now();
        return ChronoUnit.SECONDS.between(now, then);
    }
}
