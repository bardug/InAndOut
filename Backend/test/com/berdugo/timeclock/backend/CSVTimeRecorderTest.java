package com.berdugo.timeclock.backend;


import com.berdugo.timeclock.common.InAndOutDTO;
import com.berdugo.timeclock.common.InAndOutHelper;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * User: ami
 * Date: 19/05/13
 * Time: 17:13
 */
public class CSVTimeRecorderTest {

    private TimeRecorder timeRecorder;



    @org.junit.Before
    public void setUp() throws Exception {
        initLog4j();
        deleteTestDir();
        this.timeRecorder = new CSVTimeRecorderForTest();
    }

    private void deleteTestDir() throws IOException {
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();
        String dirPath = fw.getHomeDirectory().getAbsolutePath()
                + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR) +
                CSVTimeRecorder.PARENT_DIR_NOTATION + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR) +
                CSVTimeRecorderForTest.PARENT_FOLDER_TEST + System.getProperty(CSVTimeRecorder.FILE_SEPARATOR);
        try {
            Files.walkFileTree(Paths.get(dirPath), new DeletingFileVisitor());
        } catch (IOException e) {
            //do nothing
        }
    }

    private static void initLog4j() throws IOException {
        Properties loggerProperties = new Properties();
        loggerProperties.put("log4j.rootLogger", "debug, stdout");
        loggerProperties.put("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        loggerProperties.put("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        loggerProperties.put("log4j.appender.stdout.layout.ConversionPattern", "%5p [%t] (%F:%L) - %m%n");
        PropertyConfigurator.configure(loggerProperties);
    }


    @Test
    public void testBasicInAndOut() throws Exception {
        recordBasicInAndOut();
        List<InAndOutDTO> timeChart = getTimeChartFromRecorder();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int monthNum = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String today = String.valueOf(day) + "." + String.valueOf(monthNum);
        assertTrue(timeChart.get(0).getDate().equals(today));
        assertTrue(timeChart.get(0).getTotalTimeDay().equals("0:01"));
    }


    @Test
    public void testInWithNoOut() throws Exception {
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordInTime();

        List<InAndOutDTO> inAndOutDTOs = getTimeChartFromRecorder();
        assertEquals(inAndOutDTOs.size(), 3);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int monthNum = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String today = String.valueOf(day) + "." + String.valueOf(monthNum);

        InAndOutDTO inAndOutDTO0 = inAndOutDTOs.get(0);
        assertEquals(inAndOutDTO0.getDate(), today);
        assertTrue(inAndOutDTO0.getOutTime().equals(InAndOutHelper.NA_VALUE));
        InAndOutDTO inAndOutDTO1 = inAndOutDTOs.get(1);
        assertEquals(inAndOutDTO1.getDate(), today);
        InAndOutDTO inAndOutDTO2 = inAndOutDTOs.get(2);
        assertEquals(inAndOutDTO2.getDate(), today);
    }

    @Test
    public void testOutWithNoIn() throws Exception {
        timeRecorder.recordOutTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();

        List<InAndOutDTO> inAndOutDTOs = getTimeChartFromRecorder();
        assertEquals(inAndOutDTOs.size(), 3);
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int monthNum = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String today = String.valueOf(day) + "." + String.valueOf(monthNum);

        InAndOutDTO inAndOutDTO0 = inAndOutDTOs.get(0);
        assertEquals(inAndOutDTO0.getDate(), today);
        assertTrue(inAndOutDTO0.getInTime().equals(InAndOutHelper.NA_VALUE));
        InAndOutDTO inAndOutDTO1 = inAndOutDTOs.get(1);
        assertEquals(inAndOutDTO1.getDate(), today);
        InAndOutDTO inAndOutDTO2 = inAndOutDTOs.get(2);
        assertEquals(inAndOutDTO2.getDate(), today);
    }

    private List<InAndOutDTO> getTimeChartFromRecorder() throws IOException {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String month = Calendar.getInstance().getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH);
        return timeRecorder.getTimeChart(month, String.valueOf(year));
    }

    private void recordBasicInAndOut() throws IOException, InterruptedException {
        timeRecorder.recordInTime();
        Thread.sleep(60 * 1000);
        timeRecorder.recordOutTime();
    }

    private class DeletingFileVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if ( super.visitFile(file, attrs).equals(FileVisitResult.CONTINUE) ) {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            if ( super.postVisitDirectory(dir, exc).equals(FileVisitResult.CONTINUE) ) {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
            return super.postVisitDirectory(dir, exc);
        }
    };

}
