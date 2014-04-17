package com.berdugo.timeclock.frontend.time_chart;

import com.berdugo.timeclock.backend.Backend;
import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutHelper;
import com.berdugo.timeclock.frontend.InAndOutGUIHelper;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.xml.bind.ValidationException;

/**
 * Time chart table model listener
 * User: Ami
 * Date: 25/08/13
 * Time: 15:59
 */
public class TimeChartTableModelListener extends TimeChartInputsListener implements TableModelListener {

    private Backend backend;
    private Callback callback;

    public TimeChartTableModelListener(Backend backend, Callback callback, InvalidCellMarkerTable table, JLabel statusDisplayArea) {
        super(statusDisplayArea, table);
        this.backend = backend;
        this.callback = callback;
    }

    @Override
    public void tableChanged(TableModelEvent e) {

        int editedRow = table.getSelectedRow();
        int editedColumn = e.getColumn();

        TimeChartTableModel timeChartTableModel = (TimeChartTableModel) e.getSource();

        if (timeChartTableModel.isCellEditable(editedRow, editedColumn) && editedColumn != InAndOutHelper.TOTAL_COLUMN_INDEX) {

            DayInOutTuple dayInOutTuple = InAndOutGUIHelper.generateTupleFromEvent(e);

            if (!allNecessaryValuesExist(dayInOutTuple)) {
                return;
            }

            if (!inAndOutValid(editedRow)) {
                return;
            }

            try {
                validateInputs(dayInOutTuple);
            } catch (ValidationException validationException) {
                displayMessageInStatusArea(validationException.getMessage());
                table.addInvalidCell(editedRow, TimeChartDialog.DATE_COL_INDEX);
                table.addInvalidCell(editedRow, TimeChartDialog.TOTAL_TIME_COL_INDEX);
                return;
            }

            onSuccessfulValidation(editedRow, dayInOutTuple);
        }
    }

    private void onSuccessfulValidation(int editedRow, DayInOutTuple dayInOutTuple) {
        table.removeInvalidCell(editedRow, TimeChartDialog.DATE_COL_INDEX);
        table.removeInvalidCell(editedRow, TimeChartDialog.TOTAL_TIME_COL_INDEX);

        String totalString = backend.calculateTotalOfRow(dayInOutTuple, callback);

        table.getModel().setValueAt(totalString, editedRow, InAndOutHelper.TOTAL_COLUMN_INDEX);
    }

    private boolean inAndOutValid(int editedRow) {
        boolean inAndOutValid =
                table.isCellValid(editedRow, TimeChartDialog.IN_TIME_COL_INDEX) ||
                table.isCellValid(editedRow, TimeChartDialog.OUT_TIME_COL_INDEX);
        return inAndOutValid;
    }

    private void validateInputs(DayInOutTuple dayInOutTuple) throws ValidationException {
        InAndOutHelper.validateTimeString(dayInOutTuple.getInTime());
        InAndOutHelper.validateTimeString(dayInOutTuple.getOutTime());
        InAndOutHelper.validateInEarlierThanOut(dayInOutTuple);
    }

    private boolean allNecessaryValuesExist(DayInOutTuple dayInOutTuple) {

        boolean dayEmpty = dayInOutTuple.getDay().isEmpty() || dayInOutTuple.getDay().equals(InAndOutHelper.NA_VALUE);
        boolean inTimeEmpty = dayInOutTuple.getInTime().isEmpty() || dayInOutTuple.getInTime().equals(InAndOutHelper.NA_VALUE);
        boolean outTimeEmpty = dayInOutTuple.getOutTime().isEmpty() || dayInOutTuple.getOutTime().equals(InAndOutHelper.NA_VALUE);

        if (dayEmpty || inTimeEmpty || outTimeEmpty) {
            return false;
        }
        return true;
    }

}
