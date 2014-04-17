package com.berdugo.timeclock.common;

import javax.xml.bind.ValidationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * User: ami
 * Date: 19/05/13
 * Time: 20:45
 */
public class InAndOutHelper {

    public static final String DATE_COMPONENTS_DELIMITER = " ";
    public static final String DAY_MONTH_DELIMITER = ".";
    public static final String DAY_MONTH_DELIMITER_REGEX = "\\.";
    public static final String HOUR_COMPONENTS_DELIMITER = ":";

    public static final int FIRST_DAY_IN_MONTH = 1;
    public static final int MAX_DAYS_IN_MONTH = 31;
    public static final String ZERO_HOUR = "00:00";
    public static final String NA_VALUE = "N/A";
    public static final int CALENDAR_MONTH_OFFSET = 1;
    public static final int YEAR_TWO_THOUSAND = 2000;

    public static final int DAY_COLUMN_INDEX = 0;
    public static final int IN_COLUMN_INDEX = 1;
    public static final int OUT_COLUMN_INDEX = 2;
    public static final int TOTAL_COLUMN_INDEX = 3;
    public static final int ID_COLUMN_INDEX = 4;
    public static final int NUM_OF_COLUMNS = 5;


    public static long getDateMillis(String stringTime, int day) {
        Date date;
        try {
            int year = (Calendar.getInstance().get(Calendar.YEAR) % 1000) % 100;
            String yearString = String.valueOf(year);
            int month = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET;
            String monthString = month >= 10 ? String.valueOf(month) : "0" + month;
            String dayString = day >= 10 ? String.valueOf(day) : "0" + day;
            String hour = stringTime.split(HOUR_COMPONENTS_DELIMITER)[0];
            String minutes = stringTime.split(HOUR_COMPONENTS_DELIMITER)[1];
            TimeZone timeZone= TimeZone.getTimeZone(System.getProperty("user.timezone"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmm");
            formatter.setTimeZone(timeZone);
            date = formatter.parse(yearString + monthString + dayString + hour + minutes);
            date.getTime();
        } catch (ParseException e) {
            throw new RuntimeException("Formatting the date failed while updating the time chart");
        }
        return date.getTime();
    }

    public static void validateTimeString(String timeString) throws ValidationException {
        if ( ! timeString.matches("[0-2][0-9]:[0-5][0-9]") ) {
            throw new ValidationException("One of time fields is invalid (example: 02:34)");
        }
        if ( Integer.valueOf(timeString.split(InAndOutHelper.HOUR_COMPONENTS_DELIMITER)[0]) > 23 ) {
            throw new ValidationException("Hour value is greater than 23");
        }
    }

    public static void validateInEarlierThanOut(DayInOutTuple dayInOutTuple) throws ValidationException {
        String date = dayInOutTuple.getDay();
        int day = Integer.parseInt(date.split(InAndOutHelper.DAY_MONTH_DELIMITER_REGEX)[0]);

        long inMillis = InAndOutHelper.getDateMillis(dayInOutTuple.getInTime(), day);

        long outMillis = InAndOutHelper.getDateMillis(dayInOutTuple.getOutTime(), day);

        if ( (outMillis - inMillis) < 0 ) {
            throw new ValidationException("IN time must be earlier than OUT Time");
        }
    }

    public static void validateDate(String date) throws ValidationException {
        if ( ! date.matches("[0-3]*[0-9]\\.[0-1]*[0-9]") ) {
            throw new ValidationException("Date format invalid (example: 9.12)");
        }
        Integer givenMonth = Integer.valueOf(date.split(DAY_MONTH_DELIMITER_REGEX)[1]);
        int realMonth = Calendar.getInstance().get(Calendar.MONTH) + CALENDAR_MONTH_OFFSET;
        if ( ! givenMonth.equals(realMonth) ) {
            throw new ValidationException("It's illegal to type a date in another month. Month should be " + realMonth);
        }
    }

    public static String convertTotalMillisToHumanReadableTime(long millis) {
        if (millis == 0) {
            return "0";
        }

        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        String remainingMinutesString = String.valueOf(remainingMinutes);
        if (remainingMinutes < 10) {
            remainingMinutesString = "0" + remainingMinutes;
        }

        return hours + ":" + remainingMinutesString;
    }


    public static Object[][] convertDTOListInto2DArray(List<InAndOutDTO> dtoList) {
        Object[][] dto2DArray = new Object[dtoList.size()][NUM_OF_COLUMNS];
        for ( int i = 0 ; i < dtoList.size() ; i++ ) {
            InAndOutDTO dto = dtoList.get(i);
            Object[] dtoArray = new Object[NUM_OF_COLUMNS];
            dtoArray[0] = dto.getDate();
            dtoArray[1] = dto.getInTime();
            dtoArray[2] = dto.getOutTime();
            dtoArray[3] = dto.getTotalTimeDay();
            dtoArray[4] = dto.getId();
            dto2DArray[i] = dtoArray;
        }
        return dto2DArray;
    }
}
