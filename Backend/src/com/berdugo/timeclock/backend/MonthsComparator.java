package com.berdugo.timeclock.backend;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * comparator for Months in months selection
 * Created by Ami on 11/08/2014.
 */
public class MonthsComparator implements Comparator<String> {

    public static final Map<String, Integer> orderedListOfMonths = new HashMap<>(12);
    static {
        orderedListOfMonths.put("Jan", 1);
        orderedListOfMonths.put("Feb", 2);
        orderedListOfMonths.put("Mar", 3);
        orderedListOfMonths.put("Apr", 4);
        orderedListOfMonths.put("May", 5);
        orderedListOfMonths.put("Jun", 6);
        orderedListOfMonths.put("Jul", 7);
        orderedListOfMonths.put("Aug", 8);
        orderedListOfMonths.put("Sep", 9);
        orderedListOfMonths.put("Oct", 10);
        orderedListOfMonths.put("Nov", 11);
        orderedListOfMonths.put("Dec", 12);
    }

    @Override
    public int compare(String o1, String o2) {
        Integer year1 = Integer.valueOf(o1.split(" ")[0]);
        Integer year2 = Integer.valueOf(o2.split(" ")[0]);
        if ( year1.equals(year2) ) {
            String month1 = o1.split(" ")[1];
            String month2 = o2.split(" ")[1];
            return orderedListOfMonths.get(month1).compareTo(orderedListOfMonths.get(month2));
        }
        return year1.compareTo(year2);
    }
}
