package com.berdugo.timeclock.common;

import org.junit.Test;

import javax.xml.bind.ValidationException;

/**
 * test for helper
 * Created by Dev6 on 14/08/2014.
 */
public class InAndOutHelperTest {

    @Test
    public void testTimeStringValidation() {

        try {
            InAndOutHelper.validateTimeString("12:34");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }

        try {
            InAndOutHelper.validateTimeString("00:00");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }

        try {
            InAndOutHelper.validateTimeString("24:34");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }

        try {
            InAndOutHelper.validateTimeString("23:64");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }

        try {
            InAndOutHelper.validateTimeString("23:H4");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }

        try {
            InAndOutHelper.validateTimeString("23|34");
            System.out.println("PASSED");
        } catch (ValidationException e) {
            System.out.println("FAILED");
        }
    }
}

