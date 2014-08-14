package com.berdugo.timeclock.backend;

import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutDTO;
import com.berdugo.timeclock.common.InAndOutHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class CSVTimeRecorder implements TimeRecorder {

    final Logger logger = LoggerFactory.getLogger(CSVTimeRecorder.class);

    public static final String PARENT_FOLDER = "in_n_out";
    public static final String FILE_NAME_MONTH_YEAR_DELIMITER = "_";
    public static final String FILE_EXTENSION = ".csv";
    public static final String PARENT_DIR_NOTATION = "..";
	public static final String FILE_SEPARATOR = "file.separator";

    private static final String CSV_DELIMITER = ",";

    private List<InOutPair> timeChart[];

    private File thisMonthsCSV;

    private SecureRandom random;



    public CSVTimeRecorder() {
        //noinspection unchecked
        this.timeChart = new List[InAndOutHelper.MAX_DAYS_IN_MONTH];
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            // if i get here, i go home
            e.printStackTrace();
        }
    }


    @Override
	public void initRecorderMedia(String mediaName) throws IOException {
		logger.info("Initializing Recorder Media. Media is CSV File");

        File parentFolder = getCSVFilesDir();

        thisMonthsCSV = new File(parentFolder,
                mediaName +
				FILE_EXTENSION);

        logger.info("Recording time in file: " + thisMonthsCSV);

        resetTimeChart();

		if (thisMonthsCSV.exists()) {
            logger.debug(String.format("File %s was found. loading its content", thisMonthsCSV));
            loadCSV();
		}
        else if (parentFolder.exists()){
            logger.debug(String.format("File %s is not found. Creating file", thisMonthsCSV));
			createCSVFile();
		} else {
            logger.debug(String.format("File %s is not found. Creating file and all parent dirs", thisMonthsCSV));
            boolean mkdirsResult = parentFolder.mkdirs();
            if (!mkdirsResult) {
                throw new RuntimeException("Failed to create parent dirs and file");
            }
            createCSVFile();
		}
	}

    private File getCSVFilesDir() {
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();

        File csvFilesDir = new File(fw.getHomeDirectory().getAbsolutePath()
                + System.getProperty(FILE_SEPARATOR) +
                PARENT_DIR_NOTATION + System.getProperty(FILE_SEPARATOR) +
                PARENT_FOLDER + System.getProperty(FILE_SEPARATOR));

        return csvFilesDir;
    }

    @Override
    public void recordInTime() throws IOException {
        resetIfMonthBeginsToday();
        long now = System.currentTimeMillis();

        List<InOutPair> inOutPairsOfToday = getInOutPairsOfToday();
        if (inOutPairsOfToday == null) {
            inOutPairsOfToday = new ArrayList<>();
            timeChart[Calendar.getInstance().get(Calendar.DAY_OF_MONTH)] = inOutPairsOfToday;
        }
        InOutPair inOutPair = new InOutPair(now, 0L, random.nextInt());
        inOutPairsOfToday.add(inOutPair);
    }

    @Override
    public void recordOutTime() throws IOException {
        resetIfMonthBeginsToday();
        long now = System.currentTimeMillis();

        List<InOutPair> inOutPairsOfToday = getInOutPairsOfToday();
        if (inOutPairsOfToday == null) {
            inOutPairsOfToday = new ArrayList<>();
            timeChart[Calendar.getInstance().get(Calendar.DAY_OF_MONTH)] = inOutPairsOfToday;
            inOutPairsOfToday.add(new InOutPair(0L, now, random.nextInt()));
            return;
        }
        InOutPair todaysLastInOutPair = inOutPairsOfToday.get(inOutPairsOfToday.size() - 1);
        if (todaysLastInOutPair.getOutTimeMillis().equals(0L)) {
            todaysLastInOutPair.setOutTimeMillis(now);
        } else {
            inOutPairsOfToday.add(new InOutPair(0L, now, random.nextInt()));
        }
    }

    @Override
    public List<InAndOutDTO> getTimeChart() throws IOException {
        return  composeDTOList();
    }

    @Override
    public String getNameForMedia() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        return getNameForMedia(String.valueOf(year), month);
    }

    public String getNameForMedia(String month, String year) {
        StringBuilder sb = new StringBuilder();
        sb.append(month);
        sb.append(FILE_NAME_MONTH_YEAR_DELIMITER);
        sb.append(year);
        return sb.toString();
    }

    @Override
    public void updateTimeChart(List<InAndOutDTO> dtos) throws Exception {
        resetTimeChart();

        for (InAndOutDTO dto : dtos) {
            InAndOutHelper.validateDate(dto.getDate());
            int day = Integer.parseInt(dto.getDate().split(InAndOutHelper.DAY_MONTH_DELIMITER_REGEX)[0]);

            long inTimeMillis = 0;
            String inTimeString = dto.getInTime();
            if ( ! inTimeString.equals(InAndOutHelper.NA_VALUE) && ! inTimeString.isEmpty() ) {
                InAndOutHelper.validateTimeString(inTimeString);
                inTimeMillis = InAndOutHelper.getDateMillis(inTimeString, day);
            }

            long outTimeMillis = 0;
            String outTimeString = dto.getOutTime();
            if ( ! outTimeString.equals(InAndOutHelper.NA_VALUE) && ! outTimeString.isEmpty() ) {
                InAndOutHelper.validateTimeString(outTimeString);
                outTimeMillis = InAndOutHelper.getDateMillis(outTimeString, day);
            }

            List<InOutPair> inOutPairsOfDay = timeChart[day];
            if ( inOutPairsOfDay == null ) {
                inOutPairsOfDay = new ArrayList<InOutPair>();
                timeChart[day] = inOutPairsOfDay;
            }
            InOutPair inOutPair = new InOutPair(inTimeMillis, outTimeMillis, random.nextInt());
            inOutPairsOfDay.add(inOutPair);
        }

        flushIntoCSV();
    }

    @Override
    public String calculateTotal(DayInOutTuple dayInOutTuple) {
        String date = dayInOutTuple.getDay();
        int day = Integer.parseInt(date.split(InAndOutHelper.DAY_MONTH_DELIMITER_REGEX)[0]);

        long inMillis = InAndOutHelper.getDateMillis(dayInOutTuple.getInTime(), day);

        long outMillis = InAndOutHelper.getDateMillis(dayInOutTuple.getOutTime(), day);

        return InAndOutHelper.convertTotalMillisToHumanReadableTime(outMillis - inMillis);
    }

    @Override
    public String getTotalTimeForChart() {
        long totalTime = 0;
        for (List<InOutPair> timeChartEntry : timeChart) {
            if (timeChartEntry != null) {
                for (InOutPair pair : timeChartEntry) {
                    Long inTimeMillis = pair.getInTimeMillis();
                    Long outTimeMillis = pair.getOutTimeMillis();
                    if (inTimeMillis != 0 && outTimeMillis != 0) {
                        long totalTimeForDay = outTimeMillis - inTimeMillis;
                        totalTime += totalTimeForDay;
                    }
                }
            }
        }

        return InAndOutHelper.convertTotalMillisToHumanReadableTime(totalTime);
    }

    private void resetIfMonthBeginsToday() throws IOException {
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == InAndOutHelper.FIRST_DAY_IN_MONTH &&
                !thisMonthsCSV.getName().contains(getNameForMedia())) {
            flushIntoCSV();
            resetTimeChart();
            initRecorderMedia(getNameForMedia());
        }
    }

    private void  resetTimeChart() {
        for (int i = 0 ; i < InAndOutHelper.MAX_DAYS_IN_MONTH ; i ++) {
            timeChart[i] = null; // todo assigning a null value is not a good practice
        }
    }

    private List<InOutPair> getInOutPairsOfToday() {
        Integer dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return timeChart[dayOfMonth];
    }

    private List<InAndOutDTO> composeDTOList() throws IOException {

        List<InAndOutDTO> dtoList = new ArrayList<InAndOutDTO>();

        for ( int i = 0 ; i < timeChart.length ; i++ ) {
            if (timeChart[i] != null) {
                for ( InOutPair pair : timeChart[i] ) {
                    InAndOutDTO dto = new InAndOutDTO();

                    int month = Calendar.getInstance().get(Calendar.MONTH) + InAndOutHelper.CALENDAR_MONTH_OFFSET;
                    String date = String.valueOf(i) + InAndOutHelper.DAY_MONTH_DELIMITER + String.valueOf(month);
                    dto.setDate(date);

                    Long inTimeMillis = pair.getInTimeMillis();
                    Long outTimeMillis = pair.getOutTimeMillis();

                    dto.setInTime(getHourFromDate(inTimeMillis));
                    dto.setOutTime(getHourFromDate(outTimeMillis));
                    if (inTimeMillis == 0 || outTimeMillis == 0) {
                        dto.setTotalTimeDay(InAndOutHelper.NA_VALUE);
                    } else {
                       dto.setTotalTimeDay(InAndOutHelper.convertTotalMillisToHumanReadableTime(outTimeMillis - inTimeMillis));
                    }
                    dto.setId(pair.getId());
                    dtoList.add(dto);
                }
            }
        }
        return dtoList;
    }

    private String getHourFromDate(long timeInMillis) {
        if (timeInMillis == 0) {
            return InAndOutHelper.NA_VALUE;
        }
        String fullDateString = new Date(timeInMillis).toString();
        String[] fullDateArray = fullDateString.split(InAndOutHelper.DATE_COMPONENTS_DELIMITER);
        String hourMinuteSecond = fullDateArray[3];

        return hourMinuteSecond.substring(0, hourMinuteSecond.lastIndexOf(InAndOutHelper.HOUR_COMPONENTS_DELIMITER));
    }


    private void loadCSV() throws IOException {
        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(thisMonthsCSV));

            while ( (line = br.readLine()) != null ) {
                if ( !line.isEmpty() ) {
                    String[] timeRecord = line.split(CSV_DELIMITER);

                    String dayInMonthString = timeRecord[0];
                    Integer dayInMonth = Integer.valueOf(dayInMonthString);
                    List<InOutPair> inOutPairs = timeChart[dayInMonth];
                    if (inOutPairs == null) {
                        inOutPairs = new ArrayList<InOutPair>();
                        timeChart[dayInMonth] = inOutPairs;
                    }
                    inOutPairs.add(new InOutPair(Long.valueOf(timeRecord[1]), Long.valueOf(timeRecord[2]), random.nextInt()));
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("CSV file not found for READ operation");
        } finally {
            close(br);
        }
    }

    @Override
    public void flushIntoCSV() throws IOException {
        OutputStreamWriter writer = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(thisMonthsCSV, false);
            writer = new OutputStreamWriter(fos);

            for ( int i = 0 ; i < timeChart.length ; i++ ) {
                if (timeChart[i] != null) {
                    for ( InOutPair pair : timeChart[i] ) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(i)
                                .append(CSV_DELIMITER)
                                .append(pair.getInTimeMillis())
                                .append(CSV_DELIMITER)
                                .append(pair.getOutTimeMillis())
                                .append(CSV_DELIMITER)
                                .append(pair.getOutTimeMillis() - pair.getInTimeMillis());
                        writer.append(sb.toString());
                        writer.append(System.getProperty("line.separator"));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("CSV file not found for WRITE operation");
        } catch (IOException e) {
            throw new IOException("Couldn't write data to CSV file");
        } finally {
            close(writer);
            close(fos);
        }
    }

	private void createCSVFile() throws IOException {
		try {
            boolean newFileCreated = thisMonthsCSV.createNewFile();
            if (!newFileCreated) {
                throw new IOException("Trying to create an already existing CSV. This shouldn't happen");
            }
        } catch (IOException e) {
			throw new IOException("Failed to create CSV file");
		}
	}

    private void close(Closeable c) throws IOException {
        if (c == null)
            return;

        try {
            c.close();
        } catch (IOException e) {
            throw new IOException("Closing streams was unsuccessful [" + c.getClass() + "]");
        }
    }

    @Override
    public Object[] getPreviousMonths() {
        File[] csvFilesList = listRelevantFiles();
        int numOfFiles = csvFilesList.length;
        String[] previousMonths = new String[numOfFiles];
        for ( int i = 0 ; i < numOfFiles; i++ ) {
            String fileName = csvFilesList[i].getName();
            String formattedFileName = fileName.replace(FILE_NAME_MONTH_YEAR_DELIMITER, " ").substring(0, fileName.lastIndexOf("."));
            previousMonths[i] = formattedFileName;
        }

        Arrays.sort(previousMonths, new MonthsComparator());
        return previousMonths;
    }

    private File[] listRelevantFiles() {
        File csvFilesDir = getCSVFilesDir();
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("[0-9][0-9][0-9][0-9]_[a-zA-Z][a-zA-Z][a-zA-Z][.]csv");
            }
        };
        return csvFilesDir.listFiles(filenameFilter);
    }
}
