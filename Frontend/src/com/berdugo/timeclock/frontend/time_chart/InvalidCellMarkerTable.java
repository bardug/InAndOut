package com.berdugo.timeclock.frontend.time_chart;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: ami
 * Date: 26/08/13
 * Time: 08:49
 */
public class InvalidCellMarkerTable extends JTable {

    private Set<TableCell> invalidCells;
    private JComponent dependingComponent;
    private JLabel statusDisplayArea;



    public InvalidCellMarkerTable(JComponent dependingComponent, JLabel statusDisplayArea) {
        super();
        this.dependingComponent = dependingComponent;
        this.invalidCells = new HashSet<>();
        this.statusDisplayArea = statusDisplayArea;
    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component comp = super.prepareRenderer(renderer, row, column);

        if (invalidCells.contains(new TableCell(row, column))) {
            comp.setBackground(new Color(255, 200, 200));
        }
        else if (!this.isCellSelected(row, column)) {
            comp.setBackground(Color.white);
        }

        repaint();

        return comp;
    }

    /**
     * todo the name doesn't reveal the fact that the depending component is handled here
     */
    public void addInvalidCell(int row, int col) {
        invalidCells.add(new TableCell(row, col));
        if ( dependingComponent != null ) {
            disableDependantComponent();
        }
    }

    /**
     * todo the name doesn't reveal the fact that the depending component is handled here
     */
    public void removeInvalidCell(int row, int col) {
        invalidCells.remove(new TableCell(row, col));
        if ( dependingComponent != null ) {
            if ( !hasInvalidCells() ) {
                enableDependantComponent();
                clearDisplayArea();
            }
        }
    }

    public boolean isCellValid(int row, int col) {
        return !invalidCells.contains(new TableCell(row, col));
    }

    public boolean hasInvalidCells() {
        return invalidCells.size() > 0;
    }

    private void disableDependantComponent() {
        dependingComponent.setEnabled(false);
    }

    private void enableDependantComponent() {
        dependingComponent.setEnabled(true);
    }

    protected void clearDisplayArea() {
        statusDisplayArea.setText("");
    }


    private class TableCell {
        int row;
        int col;

        private TableCell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TableCell tableCell = (TableCell) o;

            if (col != tableCell.col) return false;
            if (row != tableCell.row) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = row;
            result = 31 * result + col;
            return result;
        }
    }
}
