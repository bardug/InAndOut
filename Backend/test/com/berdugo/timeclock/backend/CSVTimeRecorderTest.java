package com.berdugo.timeclock.backend;


import com.berdugo.timeclock.common.InAndOutDTO;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * User: ami
 * Date: 19/05/13
 * Time: 17:13
 */
public class CSVTimeRecorderTest {

    private TimeRecorder timeRecorder;
    private String csvFileName = "in_and_out_test";
    private File timeRecorderCSV;



    @org.junit.Before
    public void setUp() throws Exception {
        this.timeRecorder = new CSVTimeRecorder();
        timeRecorder.initRecorderMedia(csvFileName);

        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        timeRecorderCSV = new File(fw.getHomeDirectory().getAbsolutePath()
                + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR) +
                CSVTimeRecorder.PARENT_DIR_NOTATION + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR) +
                CSVTimeRecorder.PARENT_FOLDER + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR) +
                csvFileName +
                CSVTimeRecorder.FILE_EXTENSION);
    }

    @After
    public void tearDown() throws Exception {
        //noinspection ResultOfMethodCallIgnored
        timeRecorderCSV.delete();
    }

    @Test
    @Ignore
    public void testBasicInAndOut() throws Exception {
        recordBasicInAndOut();
        List<InAndOutDTO> inAndOutDTOs = timeRecorder.getTimeChart();
        InAndOutDTO inAndOutDTO = inAndOutDTOs.get(0);
        assertEquals(inAndOutDTO.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        assertEquals(inAndOutDTO.getTotalTimeDay(), "0:1");
    }

    @Test
    @Ignore
    public void testFlush() throws Exception {
        recordBasicInAndOut();
        timeRecorder.persistTimeChart();

        BufferedReader br = null;
        String line;

        try {
            br = new BufferedReader(new FileReader(timeRecorderCSV));
            line = br.readLine();
            assertTrue(line.startsWith(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + ","));
            assertTrue(Integer.valueOf(line.split(",")[3]) >= 60 * 1000 && Integer.valueOf(line.split(",")[3]) <= 70 * 1000);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("CSV file not found for READ operation");
        } finally {
            assert br != null;
            br.close();
        }
    }

    @Test
    @Ignore
    public void testLoad() throws Exception {
        recordBasicInAndOut();
        timeRecorder.persistTimeChart();
        timeRecorder.initRecorderMedia(csvFileName);
        List<InAndOutDTO> timeChart = timeRecorder.getTimeChart();
        InAndOutDTO inAndOutDTO = timeChart.get(0);
        assertEquals(inAndOutDTO.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        assertEquals(inAndOutDTO.getTotalTimeDay(), "0:1");
    }

    @Test
    @Ignore
    public void testInWithNoOut() throws Exception {
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordInTime();

        List<InAndOutDTO> inAndOutDTOs = timeRecorder.getTimeChart();
        assertEquals(inAndOutDTOs.size(), 3);
        InAndOutDTO inAndOutDTO0 = inAndOutDTOs.get(0);
        assertEquals(inAndOutDTO0.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        assertTrue(inAndOutDTO0.getOutTime().contains("1970"));
        InAndOutDTO inAndOutDTO1 = inAndOutDTOs.get(1);
        assertEquals(inAndOutDTO1.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        InAndOutDTO inAndOutDTO2 = inAndOutDTOs.get(2);
        assertEquals(inAndOutDTO2.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
    }

    @Test
    public void testOutWithNoIn() throws Exception {
        timeRecorder.recordOutTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();

        List<InAndOutDTO> inAndOutDTOs = timeRecorder.getTimeChart();
        assertEquals(inAndOutDTOs.size(), 3);
        InAndOutDTO inAndOutDTO0 = inAndOutDTOs.get(0);
        assertEquals(inAndOutDTO0.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        assertTrue(inAndOutDTO0.getInTime().contains("1970"));
        InAndOutDTO inAndOutDTO1 = inAndOutDTOs.get(1);
        assertEquals(inAndOutDTO1.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
        InAndOutDTO inAndOutDTO2 = inAndOutDTOs.get(2);
        assertEquals(inAndOutDTO2.getDate(), String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)));
    }

    private void recordBasicInAndOut() throws IOException, InterruptedException {
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();
    }
}
