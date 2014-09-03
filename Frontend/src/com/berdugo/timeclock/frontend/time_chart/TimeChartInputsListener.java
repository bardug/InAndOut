package com.berdugo.timeclock.frontend.time_chart;

import com.berdugo.gui.tables.InvalidCellMarkerTable;

import javax.swing.*;
import java.awt.*;

/**
 * abstract listener for all time chart table inputs
 * User: ami
 * Date: 29/08/13
 * Time: 16:47
 */
public abstract class TimeChartInputsListener {
    protected InvalidCellMarkerTable table;
    protected JLabel statusDisplayArea;

    public TimeChartInputsListener(JLabel statusDisplayArea, InvalidCellMarkerTable table) {
        this.statusDisplayArea = statusDisplayArea;
        this.table = table;
    }

    protected void displayMessageInStatusArea(String message) {
        statusDisplayArea.setText(message);
        statusDisplayArea.setForeground(new Color(219, 74, 55));
    }

    protected void clearDisplayArea() {
        statusDisplayArea.setText("");
    }
}
