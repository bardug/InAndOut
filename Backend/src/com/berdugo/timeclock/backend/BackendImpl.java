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
			callback.runCallbackWithText(e.getMessage());
			return;
		}
		callback.runCallback();
	}

    @Override
    public Object[][] getTimeChart(Callback callback) {
        List<InAndOutDTO> dtoList = null;
        try {
            dtoList = timeRecorder.getTimeChart();
        } catch (IOException e) {
            callback.runCallbackWithText(e.getMessage());
        }
        return InAndOutHelper.convertDTOListInto2DArray(dtoList);
    }

    @Override
    public void cleanAndClose(Callback callback) {
        try {
            timeRecorder.flushIntoCSV();
        } catch (IOException e) {
            callback.runCallbackWithText(e.getMessage());
        }
        callback.runCallback();
    }

    @Override
    public void submitTimeChart(Callback callback, Vector timeChartData) {
        try {
            timeRecorder.updateTimeChart(convertArrayToDTOList(timeChartData));
        } catch (Exception e) {
            callback.runCallbackWithText("Failed to update time chart. reason: " + e.getMessage());
            logger.error("Failed to update time chart. reason: " + e.getMessage());
            return;
        }
        callback.runCallback();
    }

    @Override
    public String calculateTotalOfRow(DayInOutTuple dayInOutTuple, Callback callback) {
        String s = InAndOutHelper.NA_VALUE;
        try {
            s = timeRecorder.calculateTotal(dayInOutTuple);
        } catch (Exception e) {
            callback.runCallbackWithText(e.getMessage());
        }

        return s;
    }

    @Override
    public String getTotalTimeForChart(Callback callback) {
        String totalTimeForChart = timeRecorder.getTotalTimeForChart();
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
        Object[] previousMonths = timeRecorder.getPreviousMonths();
        return previousMonths;
    }

    @Override
    public Object[][] loadPreviousMonth(String selectedMonth, Callback callback) {
        String[] monthYearArray = selectedMonth.split(" ");
        String month = monthYearArray[0];
        String year = monthYearArray[1];
        String mediaName = timeRecorder.getNameForMedia(month, year);
        List<InAndOutDTO> dtoList = null;
        try {
            timeRecorder.initRecorderMedia(mediaName);
            dtoList = timeRecorder.getTimeChart();
        } catch (IOException e) {
            callback.runCallbackWithText(e.getMessage());
        }
        return InAndOutHelper.convertDTOListInto2DArray(dtoList);
    }
}
