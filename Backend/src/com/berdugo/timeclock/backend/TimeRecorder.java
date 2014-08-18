package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutDTO;
import com.berdugo.timeclock.common.TimeChartStatistics;

import java.io.IOException;
import java.util.List;

public interface TimeRecorder {

    void initRecorderMedia(String mediaName) throws IOException;

    public void recordInTime() throws IOException;

    public void recordOutTime() throws IOException;

    List<InAndOutDTO> getTimeChart() throws IOException;

    void persistTimeChart() throws IOException;

    String getNameForMedia();

    String getNameForMedia(String month, String year);

    void updateTimeChart(List<InAndOutDTO> dtos) throws Exception;

    String calculateTotal(DayInOutTuple dayInOutTuple);

    TimeChartStatistics getTimeChartStatistics();

    Object[] getPreviousMonths();
}
