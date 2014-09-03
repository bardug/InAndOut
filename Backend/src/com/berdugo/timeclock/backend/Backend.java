package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.TimeChartStatistics;

import java.util.Vector;

public interface Backend {
	
	void signIn(Callback callback);
	
	void signOut(Callback callback);

    void closeTimeChart(Callback callback);

    void cleanAndClose(Callback callback);

    void submitTimeChart(Callback callback, Vector timeChartData);

    String calculateTotalOfRow(DayInOutTuple dayInOutTuple, Callback callback);

    TimeChartStatistics getTimeChartStatistics(Callback callback);

    Object[] getMonthList(Callback callback);

    Object[][] loadMonth(String selectedMonth, Callback callback);
}