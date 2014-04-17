package com.berdugo.timeclock.common;

/**
 * User: ami
 * Date: 19/05/13
 * Time: 09:30
 */
public class InAndOutDTO {
    private Integer id;
    private String date;
    private String inTime;
    private String outTime;
    private String totalTimeDay;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getTotalTimeDay() {
        return totalTimeDay;
    }

    public void setTotalTimeDay(String totalTimeDay) {
        this.totalTimeDay = totalTimeDay;
    }
}
