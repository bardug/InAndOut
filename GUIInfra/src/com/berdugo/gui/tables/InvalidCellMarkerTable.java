package com.berdugo.gui.tables;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * a table that can mark cells with invalid values
 * User: ami
 * Date: 26/08/13
 * Time: 08:49
 */
public class InvalidCellMarkerTable extends JTable {

    private Set<TableCell> invalidCells;
    private Component[] dependingComponents;



    public InvalidCellMarkerTable(Component... dependingComponents) {
        super();
        this.dependingComponents = dependingComponents;
        this.invalidCells = new HashSet<>();
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
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
        if ( dependingComponents != null ) {
            disableDependantComponent();
        }
    }

    /**
     * todo the name doesn't reveal the fact that the depending component is handled here
     */
    public void removeInvalidCell(int row, int col) {
        invalidCells.remove(new TableCell(row, col));
        if ( dependingComponents != null ) {
            if ( !hasInvalidCells() ) {
                enableDependantComponent();
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
        for ( Component component : dependingComponents ) {
            component.setEnabled(false);
        }
    }

    private void enableDependantComponent() {
        for ( Component component : dependingComponents ) {
            component.setEnabled(true);
        }
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
