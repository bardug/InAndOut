package com.berdugo.timeclock.common;

/**
 * Day, In time, Out time tuple
 * User: Ami
 * Date: 25/08/13
 * Time: 17:18
 */
public class DayInOutTuple {
    String day;
    String inTime;
    String outTime;

    public DayInOutTuple(String day, String inTime, String outTime) {
        this.day = day;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public String getDay() {
        return day;
    }

    public String getInTime() {
        return inTime;
    }

    public String getOutTime() {
        return outTime;
    }
}
