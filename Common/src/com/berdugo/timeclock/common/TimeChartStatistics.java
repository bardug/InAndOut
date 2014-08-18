package com.berdugo.timeclock.common;

/**
 * statistics object of time chart
 * Created by ami on 17/08/2014.
 */
public class TimeChartStatistics {
    private String totalTimeForChart;
    private Integer numOfFullyReportedDays;
    private String avgTimePerDay;

    public TimeChartStatistics(String totalTimeForChart, Integer numOfFullyReportedDays, String avgTimePerDay) {
        this.totalTimeForChart = totalTimeForChart;
        this.numOfFullyReportedDays = numOfFullyReportedDays;
        this.avgTimePerDay = avgTimePerDay;
    }

    public String getTotalTimeForChart() {
        return totalTimeForChart;
    }

    public Integer getNumOfFullyReportedDays() {
        return numOfFullyReportedDays;
    }

    public String getAvgTimePerDay() {
        return avgTimePerDay;
    }

    @Override
    public String toString() {
        return "TimeChartStatistics{" +
                "totalTimeForChart='" + totalTimeForChart + '\'' +
                ", numOfFullyReportedDays=" + numOfFullyReportedDays +
                ", avgTimePerDay='" + avgTimePerDay + '\'' +
                '}';
    }
}
