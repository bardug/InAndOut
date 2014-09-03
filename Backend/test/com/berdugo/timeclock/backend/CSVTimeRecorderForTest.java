package com.berdugo.timeclock.backend;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * inherits {@link CSVTimeRecorder} and used for tests
 * Created by ami on 02/09/2014.
 */
public class CSVTimeRecorderForTest extends CSVTimeRecorder {

    public static final String PARENT_FOLDER_TEST = PARENT_FOLDER + "_test";

    @Override
    protected File getCSVFilesDir() {
        JFileChooser fr = new JFileChooser();
        FileSystemView fw = fr.getFileSystemView();

        File csvFilesDir = new File(fw.getHomeDirectory().getAbsolutePath()
                + System.getProperty(FILE_SEPARATOR) +
                PARENT_DIR_NOTATION + System.getProperty(FILE_SEPARATOR) +
                PARENT_FOLDER_TEST + System.getProperty(FILE_SEPARATOR));

        return csvFilesDir;
    }

}
