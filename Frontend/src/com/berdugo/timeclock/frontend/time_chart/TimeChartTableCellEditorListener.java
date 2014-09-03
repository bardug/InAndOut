package com.berdugo.timeclock.frontend.time_chart;


import com.berdugo.gui.tables.InvalidCellMarkerTable;
import com.berdugo.timeclock.common.InAndOutHelper;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.xml.bind.ValidationException;

/**
 * Time chart table cell editor listener <p/>
 * validates user inputs in table
 * User: ami
 * Date: 29/08/13
 * Time: 09:16
 */
public abstract class TimeChartTableCellEditorListener extends TimeChartInputsListener implements CellEditorListener {

    /**
     * (constructor)
     * @param statusDisplayArea the area to display errors/status in table panel
     */
    public TimeChartTableCellEditorListener(InvalidCellMarkerTable table, JLabel statusDisplayArea) {
        super(statusDisplayArea, table);
    }

    @Override
    public void editingStopped(ChangeEvent e) {

        DefaultCellEditor cellEditor = (DefaultCellEditor) e.getSource();

        try {

            String cellEditorValue = (String) cellEditor.getCellEditorValue();
            if ( !cellEditorValue.equals(InAndOutHelper.NA_VALUE) ) {
                validateInput(cellEditorValue);
            }

        } catch (ValidationException validationException) {
            displayMessageInStatusArea(validationException.getMessage());
            table.addInvalidCell(table.getSelectedRow(), table.getSelectedColumn());
            return;
        }

        table.removeInvalidCell(table.getSelectedRow(), table.getSelectedColumn());
        clearDisplayArea();
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
    }


    protected abstract void validateInput(String time) throws ValidationException;

}
