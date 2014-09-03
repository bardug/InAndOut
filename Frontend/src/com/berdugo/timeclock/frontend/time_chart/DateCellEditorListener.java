package com.berdugo.timeclock.frontend.time_chart;


import com.berdugo.gui.tables.InvalidCellMarkerTable;
import com.berdugo.timeclock.common.InAndOutHelper;

import javax.swing.*;
import javax.xml.bind.ValidationException;

/**
 * validates date fields in table
 * User: Ami
 * Date: 25/08/13
 * Time: 15:59
 */
public class DateCellEditorListener extends TimeChartTableCellEditorListener {


    public DateCellEditorListener(InvalidCellMarkerTable table, JLabel statusDisplayArea) {
        super(table, statusDisplayArea);
    }

    protected void validateInput(String time) throws ValidationException {

        InAndOutHelper.validateDate(time);

    }
}
