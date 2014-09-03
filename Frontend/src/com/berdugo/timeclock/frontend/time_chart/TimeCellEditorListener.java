package com.berdugo.timeclock.frontend.time_chart;


import com.berdugo.gui.tables.InvalidCellMarkerTable;
import com.berdugo.timeclock.common.InAndOutHelper;

import javax.swing.*;
import javax.xml.bind.ValidationException;

/**
 * validates time fields in table
 * User: Ami
 * Date: 25/08/13
 * Time: 15:59
 */
public class TimeCellEditorListener extends TimeChartTableCellEditorListener {


    public TimeCellEditorListener(InvalidCellMarkerTable table, JLabel statusDisplayArea) {
        super(table, statusDisplayArea);
    }

    protected void validateInput(String time) throws ValidationException {

        InAndOutHelper.validateTimeString(time);

    }
}
