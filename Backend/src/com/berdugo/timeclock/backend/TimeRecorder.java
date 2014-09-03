package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutDTO;
import com.berdugo.timeclock.common.TimeChartStatistics;

import java.io.IOException;
import java.util.List;

public interface TimeRecorder {

    void closeTimeChart() throws IOException;

    public void recordInTime() throws IOException;

    public void recordOutTime() throws IOException;

    List<InAndOutDTO> getTimeChart(String month, String year) throws IOException;

    void persistTimeChart() throws IOException;

    void updateTimeChart(List<InAndOutDTO> dtos) throws Exception;

    String calculateTotal(DayInOutTuple dayInOutTuple);

    TimeChartStatistics getTimeChartStatistics();

    Object[] getPreviousMonths();
}
