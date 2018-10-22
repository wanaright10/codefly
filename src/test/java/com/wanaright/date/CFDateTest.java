package com.wanaright.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CFDateTest {
    @Test
    public void between() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextYear = now.plusYears(1);

        Assert.assertEquals(CFDate.between(now, nextYear, ChronoUnit.YEARS), 1);
        Assert.assertEquals(CFDate.between(nextYear, now, ChronoUnit.YEARS), -1);
    }
}