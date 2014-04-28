package com.berdugo.timeclock.frontend.time_chart;

import com.berdugo.gui.buttons.BlueButton;
import com.berdugo.timeclock.backend.Backend;
import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.frontend.InAndOutErrorHandler;
import com.berdugo.timeclock.frontend.InAndOutGUIHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;

public class TimeChartDialog extends JDialog {

    public static final int DATE_COL_INDEX = 0;
    public static final int IN_TIME_COL_INDEX = 1;
    public static final int OUT_TIME_COL_INDEX = 2;
    public static final int TOTAL_TIME_COL_INDEX = 3;

    private Backend backend;

    private static final long serialVersionUID = 1L;
    private JTable table;
//    private JButton cancelButton;
    private BlueButton cancelButton;
//    private JButton saveButton;
    private BlueButton saveButton;
    private JLabel statusLabel;

    public TimeChartDialog(Backend backend) {
        this.backend = backend;
        buildUI();
    }

    private void buildUI() {
        prepareToolbar();

        prepareLowerPanel();

        prepareInnerTable();

        setDialogAttributes();

        addListeners();
    }

    private void prepareToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setLayout(new GridBagLayout());

        GridBagConstraints eastConstraints = new GridBagConstraints();
        eastConstraints.anchor = GridBagConstraints.EAST;
        eastConstraints.weightx = 1;

        GridBagConstraints westConstraints = new GridBagConstraints();
        westConstraints.anchor = GridBagConstraints.WEST;
        westConstraints.weightx = 0;

        toolBar.add(getAddRowButton(), westConstraints);
        toolBar.addSeparator();
        toolBar.add(getDeleteRowButton(), westConstraints);
        toolBar.addSeparator();
        statusLabel = new JLabel();
        toolBar.add(statusLabel, eastConstraints);
        getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    private void prepareInnerTable() {

        prepareTable();

        getContentPane().add(new JScrollPane(table));

        prepareHeaderRenderer();

        prepareModel();

        fixColumnsAndPrepareCellAttributes();
    }

    private void prepareTable() {
        table = new InvalidCellMarkerTable(saveButton, statusLabel);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
    }

    private void prepareModel() {
        Object[][] tableModel2DArray = backend.getTimeChart(new TimeChartDialogCallback());
        table.setModel(new TimeChartTableModel(tableModel2DArray));

        addModelListener();
    }

    private void fixColumnsAndPrepareCellAttributes() {
        table.removeColumn(table.getColumn("ID"));

        prepareCellAttributes();
    }

    private void addModelListener() {
        TableModelListener timeChartTableModelListener = new TimeChartTableModelListener(backend, new TimeChartDialogCallback(), (InvalidCellMarkerTable) table, statusLabel);
        table.getModel().addTableModelListener(timeChartTableModelListener);
    }

    private void prepareCellAttributes() {
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for ( int i = 0 ; i < table.getColumnCount() ; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(centerRenderer);
            DefaultCellEditor cellEditor = new DefaultCellEditor(new JTextField());
            if (i == DATE_COL_INDEX ) {
                cellEditor.addCellEditorListener(new DateCellEditorListener((InvalidCellMarkerTable) table, statusLabel));
            }
            if (i == IN_TIME_COL_INDEX || i == OUT_TIME_COL_INDEX ) {
                cellEditor.addCellEditorListener(new TimeCellEditorListener((InvalidCellMarkerTable) table, statusLabel));
            }
            column.setCellEditor(cellEditor);
        }
    }

    private void prepareHeaderRenderer() {
        table.getTableHeader().setDefaultRenderer(new DefaultTableHeaderCellRenderer());
    }

    private void prepareLowerPanel() {
        JPanel lowerPanel = new JPanel();
        lowerPanel.setLayout(new BorderLayout());

        lowerPanel.add(getButtonsPanel(), BorderLayout.SOUTH);

        JPanel loadMonthPanel = getLoadMonthPanel();
        lowerPanel.add(loadMonthPanel,BorderLayout.NORTH);

        getContentPane().add(lowerPanel, BorderLayout.SOUTH);
    }

    private JPanel getLoadMonthPanel() {
        JPanel loadMonthPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        loadMonthPanel.add(new JLabel("Load some other month:"));
        Object[] previousMonths = getPreviousMonths();
        JComboBox<Object> monthsCombo = new JComboBox<>(previousMonths);
        monthsCombo.addActionListener(getComboBoxListener());
        loadMonthPanel.add(monthsCombo);
        return loadMonthPanel;
    }

    private Object[] getPreviousMonths() {
        return backend.getPreviousMonths(new Callback() {
                @Override
                public void runCallback() {
                }

                @Override
                public void runCallbackWithText(String text) {
                    InAndOutErrorHandler.popErrorDialog(text, getContentPane());
                }
            });
    }

    private ActionListener getComboBoxListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedMonth = (String) ((JComboBox) e.getSource()).getSelectedItem();
                Object[][] previousMonthTimeChart = backend.loadPreviousMonth(selectedMonth, new Callback() {
                    @Override
                    public void runCallback() {
                    }

                    @Override
                    public void runCallbackWithText(String text) {
                        InAndOutErrorHandler.popErrorDialog(text, getContentPane());
                    }
                });
                table.setModel(new TimeChartTableModel(previousMonthTimeChart));
                fixColumnsAndPrepareCellAttributes();
            }
        };
    }

    private JPanel  getButtonsPanel() {
        JPanel buttonsPane = new JPanel();
        buttonsPane.setLayout(new GridBagLayout());


        GridBagConstraints westConstraints = new GridBagConstraints();
        westConstraints.anchor = GridBagConstraints.WEST;
        westConstraints.weightx = 1;
        westConstraints.insets = new Insets(0,5,5,5);
        westConstraints.ipady = 4;
        westConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel totalLabel = getTotalTimeLabel();
        totalPanel.setBorder(new TitledBorder(""));
        totalPanel.add(totalLabel);
        buttonsPane.add(totalPanel, westConstraints);

        GridBagConstraints eastConstraints = new GridBagConstraints();
        eastConstraints.anchor = GridBagConstraints.EAST;
        eastConstraints.weightx = 0;
        eastConstraints.insets = new Insets(0,5,5,5);

        initSaveButton();
        buttonsPane.add(saveButton, eastConstraints);

        initCancelButton();
        eastConstraints.insets = new Insets(0,0,5,5);
        buttonsPane.add(cancelButton, eastConstraints);


        return buttonsPane;
    }

    private JLabel getTotalTimeLabel() {
        String totalTimeForChart = backend.getTotalTimeForChart(new Callback() {
            @Override
            public void runCallback() {
            }

            @Override
            public void runCallbackWithText(String text) {
            }
        });

        return new JLabel("Total Time: " + totalTimeForChart);
    }

    private JButton getDeleteRowButton() {
        JButton delRowButton = new JButton();
        delRowButton.setActionCommand("Delete Rows");
        delRowButton.setIcon(new ImageIcon(InAndOutGUIHelper.getDeleteIcon(), "Delete Rows"));
        delRowButton.setToolTipText("Delete Rows");
        delRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRowsFromTable();
            }
        });
        return delRowButton;
    }

    private void deleteSelectedRowsFromTable() {
        int[] selectedRowsIndexes = table.getSelectedRows();

        for (int i = 0 ; i < selectedRowsIndexes.length ; i++) {
            ((TimeChartTableModel) table.getModel()).removeRow(selectedRowsIndexes[i] - i);
        }
    }

    private JButton getAddRowButton() {
        JButton addRowButton = new JButton();
        addRowButton.setActionCommand("Add Row");
        addRowButton.setIcon(new ImageIcon(InAndOutGUIHelper.getAddIcon(), "Add Row"));
        addRowButton.setToolTipText("Add Row");
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                addRowToTable();
            }
        });
        return addRowButton;

    }

    private void addRowToTable() {
        ((TimeChartTableModel)table.getModel()).addRow(new Object[]{"","","","",""});
        ((TimeChartTableModel)table.getModel()).setLastRowEditable(true);
    }

    private BlueButton initSaveButton() {
        saveButton = new BlueButton("Save & Close");
//        saveButton.setActionCommand("Save & Close");
//        saveButton.setIcon(new ImageIcon(InAndOutGUIHelper.getSaveIcon(), "Save & Close"));
        saveButton.setToolTipText("Save & Close");
        saveButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ( saveButton.isEnabled() ) {
                    final Component ancestor = (Component) e.getSource();
                    backend.submitTimeChart(new Callback() {
                        @Override
                        public void runCallback() {
                            Window win = SwingUtilities.getWindowAncestor(ancestor);
                            win.dispose();
                        }

                        @Override
                        public void runCallbackWithText(String text) {
                            InAndOutErrorHandler.popErrorDialog(text, getContentPane());
                        }
                    }, ((TimeChartTableModel) table.getModel()).getDataVector());
                }
            }
        });

/*
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
            }
        });
*/
        return saveButton;
    }

    private BlueButton initCancelButton() {
        cancelButton = new BlueButton("Cancel");
        cancelButton.setToolTipText("Cancel");
        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
                win.dispose();
            }
        });
        return cancelButton;
    }

/*
    private JButton initCancelButton() {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.setToolTipText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
                win.dispose();
            }
        });
        return cancelButton;
    }
*/

    private class TimeChartDialogCallback implements Callback {
        @Override
        public void runCallback() {
            ((TimeChartTableModel)table.getModel()).setLastRowEditable(false);
        }

        @Override
        public void runCallbackWithText(String text) {
            InAndOutErrorHandler.popErrorDialog(text, getContentPane());
        }
    }

    private void setDialogAttributes() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Time Sheet");
        getOwner().setIconImages(InAndOutGUIHelper.getTimeChartImages());
        pack();
    }

    private void addListeners() {
        addWindowListener(
                new WindowAdapter() {
                    public void windowOpened(WindowEvent e) {
                        cancelButton.requestFocus();
                    }
                    public void windowClosing(WindowEvent e) {
                        dispose();
                    }
                });
    }
}
