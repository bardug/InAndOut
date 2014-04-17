package com.berdugo.timeclock.bakcend;


import com.berdugo.timeclock.common.InAndOutHelper;
import org.junit.Test;

import javax.xml.bind.ValidationException;

/**
 * User: Ami
 * Date: 27/05/13
 * Time: 20:26
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
