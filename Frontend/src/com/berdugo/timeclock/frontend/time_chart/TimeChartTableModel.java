package com.berdugo.timeclock.frontend.time_chart;


import com.berdugo.timeclock.common.InAndOutHelper;

import javax.swing.table.DefaultTableModel;

public class TimeChartTableModel extends DefaultTableModel {

    public static Object[] TIME_CHART_COL_NAMES = new Object[] {"Date", "IN", "OUT", "Total", "ID"};
    private boolean lastRowEditable;

    public TimeChartTableModel() {
        super(TIME_CHART_COL_NAMES, 0);
        lastRowEditable = false;
    }

    public Class getColumnClass(int columnIndex) {
        Class[] columnTypes = new Class[] {
                String.class, String.class, String.class, String.class, Integer.class
        };
        return columnTypes[columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if ( lastRowEditable && row == this.getRowCount()-1 ) {
            return true;
        }
        switch (column) {
            case InAndOutHelper.DAY_COLUMN_INDEX:
            case InAndOutHelper.TOTAL_COLUMN_INDEX:
                return false;
            case InAndOutHelper.IN_COLUMN_INDEX:
            case InAndOutHelper.OUT_COLUMN_INDEX:
                return true;

        }
        return false;
    }

    public void setLastRowEditable(boolean lastRowEditable) {
        this.lastRowEditable = lastRowEditable;
    }
}
