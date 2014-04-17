package com.berdugo.timeclock.frontend;

import com.berdugo.timeclock.common.DayInOutTuple;
import com.berdugo.timeclock.common.InAndOutHelper;
import com.berdugo.timeclock.frontend.time_chart.TimeChartTableModel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Helper for GUI classes
 * User: ami
 * Date: 26/08/13
 * Time: 10:07
 */
public class InAndOutGUIHelper {

    public static Font DEFAULT_FONT = UIManager.getDefaults().getFont("TextPane.font");

    public static DayInOutTuple generateTupleFromEvent(TableModelEvent e) {
        int editedRow = e.getFirstRow();
        TimeChartTableModel timeChartTableModel = (TimeChartTableModel) e.getSource();

        String dayFromEditedRow = (String)((Vector) timeChartTableModel.getDataVector().get(editedRow)).get(InAndOutHelper.DAY_COLUMN_INDEX);
        String inTimeFromEditedRow = (String)((Vector) timeChartTableModel.getDataVector().get(editedRow)).get(InAndOutHelper.IN_COLUMN_INDEX);
        String outTimeFromEditedRow = (String)((Vector) timeChartTableModel.getDataVector().get(editedRow)).get(InAndOutHelper.OUT_COLUMN_INDEX);

        return new DayInOutTuple(dayFromEditedRow, inTimeFromEditedRow, outTimeFromEditedRow);
    }


    public static List<Image> getImages() {

        Image image = getIconLarge();
        Image image16 = getIconSmall();
        java.util.List<Image> images = new ArrayList<Image>(2);
        images.add(image16);
        images.add(image);

        return images;
    }

    public static List<Image> getTimeChartImages() {

        Image image = getTimeChartIconLarge();
        Image image16 = getTimeChartIconSmall();
        java.util.List<Image> images = new ArrayList<Image>(2);
        images.add(image16);
        images.add(image);

        return images;
    }


    public static Image getTimeChartIconSmall() {
        return getIconByPath("/res/timesheet_icon_16.png");
    }

    public static Image getTimeChartIconLarge() {
        return getIconByPath("/res/timesheet_icon.png");
    }
    
    public static Image getIconSmall() {
        return getIconByPath("/res/clock_16.png");
    }

    public static Image getIconLarge() {
        return getIconByPath("/res/clock.png");
    }

    public static Image getAddIcon() {
        return getIconByPath("/res/edit_add.png");
    }

    public static Image getDeleteIcon() {
        return getIconByPath("/res/delete.png");
    }

    public static Image getSaveIcon() {
        return getIconByPath("/res/save.png");
    }

    public static Image getBatteryFullIcon() {
        return getIconByPath("/res/battery_full.png");
    }

    public static Image getBatteryFullSmallerIcon() {
        return getIconByPath("/res/battery_full_smaller.png");
    }

    public static Image getBatteryEmptyIcon() {
        return getIconByPath("/res/battery_empty.png");
    }

    public static Image getBatteryEmptySmallerIcon() {
        return getIconByPath("/res/battery_empty_smaller.png");
    }

    public static Image getEditIcon() {
        return getIconByPath("/res/search_doc_32.png");
    }

    public static Image getEditSmallerIcon() {
        return getIconByPath("/res/search_doc_32_smaller.png");
    }

    private static Image getIconByPath(String path) {
        ImageIcon imageIcon = new ImageIcon(InAndOutHelper.class.getClass().getResource(path));
        return imageIcon.getImage();
    }
}
