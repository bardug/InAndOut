package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutDTO;
import com.berdugo.timeclock.common.InAndOutHelper;
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
	public void initTimeChart(Callback callback) {
		try {
			timeRecorder.initRecorderMedia(timeRecorder.getNameForMedia());
		} catch (IOException e) {
            logger.error(e.getMessage());
			callback.runCallbackWithText(e.getMessage());
			return;
		}
		callback.runCallback();
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
    public Object[][] getTimeChart(Callback callback) {
        logger.info("getting time chart...");
        List<InAndOutDTO> dtoList = null;
        try {
            dtoList = timeRecorder.getTimeChart();
        } catch (IOException e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("time chart fetched");
        return InAndOutHelper.convertDTOListInto2DArray(dtoList);
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
            callback.runCallbackWithText("Failed to update time chart. reason: " + e.getMessage());
            logger.error("Failed to update time chart. reason: " + e.getMessage());
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
    public String getTotalTimeForChart(Callback callback) {
        String totalTimeForChart = null;
        try {
            totalTimeForChart = timeRecorder.getTotalTimeForChart();
        } catch (Exception e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("Getting total time for chart; Result: [" + totalTimeForChart + "]");
        return totalTimeForChart;
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
    public Object[] getPreviousMonths(Callback callback) {
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
    public Object[][] loadPreviousMonth(String selectedMonth, Callback callback) {
        logger.info("loading time sheet for " + selectedMonth + "...");
        String[] monthYearArray = selectedMonth.split(" ");
        String month = monthYearArray[0];
        String year = monthYearArray[1];
        String mediaName = timeRecorder.getNameForMedia(month, year);
        logger.info("media info of time sheet: " + mediaName);
        List<InAndOutDTO> dtoList = null;
        try {
            timeRecorder.initRecorderMedia(mediaName);
            dtoList = timeRecorder.getTimeChart();
        } catch (IOException e) {
            logger.error(e.getMessage());
            callback.runCallbackWithText(e.getMessage());
        }
        logger.info("time sheet for " + selectedMonth + " was loaded");
        return InAndOutHelper.convertDTOListInto2DArray(dtoList);
    }
}
