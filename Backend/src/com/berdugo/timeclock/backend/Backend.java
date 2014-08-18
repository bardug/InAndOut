package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.TimeChartStatistics;

import java.util.Vector;

public interface Backend {
	
	void initTimeChart(Callback callback);
	
	void signIn(Callback callback);
	
	void signOut(Callback callback);

    Object[][] getTimeChart(Callback callback);

    void cleanAndClose(Callback callback);

    void submitTimeChart(Callback callback, Vector timeChartData);

    String calculateTotalOfRow(DayInOutTuple dayInOutTuple, Callback callback);

    TimeChartStatistics getTimeChartStatistics(Callback callback);

    Object[] getPreviousMonths(Callback callback);

    Object[][] loadPreviousMonth(String selectedMonth, Callback callback);
}