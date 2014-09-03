package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BackendImpl implements Backend {

    final Logger logger = LoggerFactory.getLogger(BackendImpl.class);

    private TimeRecorder timeRecorder;
	

	
	
	public BackendImpl() {
		super();
		timeRecorder = new CSVTimeRecorder();
	}


	@Override
	public void signIn(Callback callback) {	
		try {
			timeRecorder.recordInTime();
		} catch (IOException e) {
            logger.error(e.getMessage());
			callback.runCallbackWithText(e.getMessage());
			return;
		}
		callback.runCallback();
	}


	@Override
	public void signOut(Callback callback) {
		try {
			timeRecorder.recordOutTime();
		} catch (IOException e) {
            logger.error(e.getMessage());
			callback.runCallbackWithText(e.getMessage());
			return;
		}
		callback.runCallback();
	}

    @Override
    public void closeTimeChart(Callback callback) {
        logger.info("closing time chart...");
        try {
            timeRecorder.closeTimeChart();
        } catch (IOException e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("time chart data was closed");
        callback.runCallback();
    }

    @Override
    public void cleanAndClose(Callback callback) {
        logger.info("writing time chart data permanently...");
        try {
            timeRecorder.persistTimeChart();
        } catch (IOException e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("time chart data was written");
        callback.runCallback();
    }

    @Override
    public void submitTimeChart(Callback callback, Vector timeChartData) {
        logger.info("submitting time chart...");
        try {
            timeRecorder.updateTimeChart(convertArrayToDTOList(timeChartData));
        } catch (Exception e) {
            callback.runCallbackWithText("Failed to update time chart. reason: " + e);
            logger.error("Failed to update time chart. reason: " + e);
            return;
        }
        logger.info("time chart submitted");
        callback.runCallback();
    }

    @Override
    public String calculateTotalOfRow(DayInOutTuple dayInOutTuple, Callback callback) {
        logger.debug("calculating total of row for day: " + dayInOutTuple.getDay());
        String s = InAndOutHelper.NA_VALUE;
        try {
            s = timeRecorder.calculateTotal(dayInOutTuple);
        } catch (Exception e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.debug("total for day: " + dayInOutTuple.getDay() + ": " + s);
        return s;
    }

    @Override
    public TimeChartStatistics getTimeChartStatistics(Callback callback) {
        TimeChartStatistics timeChartStatistics = null;
        try {
            timeChartStatistics = timeRecorder.getTimeChartStatistics();
        } catch (Exception e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        assert timeChartStatistics != null;
        logger.info("Getting statistics for time chart; Result: [" + timeChartStatistics.toString() + "]");
        return timeChartStatistics;
    }

    private List<InAndOutDTO> convertArrayToDTOList(Vector timeChartData) {
        List<InAndOutDTO> dtos = new ArrayList<InAndOutDTO>();

        for (Object rowObject : timeChartData) {
            Vector rowVector = (Vector) rowObject;
            InAndOutDTO dto = new InAndOutDTO();
            dto.setDate((String)rowVector.get(0));
            dto.setInTime((String)rowVector.get(1));
            dto.setOutTime(((String)rowVector.get(2)));
            dto.setTotalTimeDay((String)rowVector.get(3));
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public Object[] getMonthList(Callback callback) {
        logger.info("getting previous months selection...");
        Object[] previousMonths = new Object[0];
        try {
            previousMonths = timeRecorder.getPreviousMonths();
        } catch (Exception e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("previous months were loaded: " + previousMonths);
        return previousMonths;
    }

    @Override
    public Object[][] loadMonth(String selectedMonth, Callback callback) {
        logger.info("loading time sheet for " + selectedMonth + "...");
        String[] monthYearArray = selectedMonth.split(" ");
        String year = monthYearArray[0];
        String month = monthYearArray[1];
        List<InAndOutDTO> dtoList = null;
        try {
            dtoList = timeRecorder.getTimeChart(month, year);
        } catch (IOException e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("time sheet for " + selectedMonth + " was loaded");
        return InAndOutHelper.convertDTOListInto2DArray(dtoList);
    }
}
