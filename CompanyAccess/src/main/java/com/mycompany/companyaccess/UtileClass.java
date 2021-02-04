/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author CORI
 */
public class UtileClass {

    public static final long REQUIRED_WORKING_HOURS = 8 * 3600000; // in ms

    public static String convertMstoActualDate(long miliseconds) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");

        Date result = new Date(miliseconds);

        System.out.println(simple.format(result));
        return simple.format(result);
    }

    /**
     *
     * @param milliseconds
     * @return the milliseconds converted to HH:mm
     */
    public static String msToHHmm(long milliseconds) {
        if (0 == milliseconds) {
            return "-";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date resultdate = new Date(milliseconds);
            return sdf.format(resultdate);
        }
    }

    public static String computeDuration(long milliseconds) {

        return DurationFormatUtils.formatDuration(milliseconds, "HH:mm");
    }

    public static String getCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public static void main(String args[]) {

        long entryTime = System.currentTimeMillis();

        System.out.println("Good in time " + msToHHmm(entryTime - 13 * 3600000));

        System.out.println("on millies " + entryTime);
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm");

        // Creating date from milliseconds 
        // using Date() constructor 
        Date result = new Date(entryTime - 13 * 3600000);

        // Formatting Date according to the 
        // given format 
        System.out.println(simple.format(result));

        long exitTime = System.currentTimeMillis() - 5 * 3600000;

        System.out.println("exit in time " + convertMstoActualDate(exitTime));

        System.out.println("on millies " + exitTime);

        System.out.println("DIFF " + (exitTime - entryTime));
        System.out.println(DurationFormatUtils.formatDuration((exitTime - entryTime), "HH:mm"));

        long miliSec = System.currentTimeMillis();

        // Creating date format 
        simple = new SimpleDateFormat("dd MMM yyyy HH:mm");

        // Creating date from milliseconds 
        // using Date() constructor 
        result = new Date(miliSec);

        // Formatting Date according to the 
        // given format 
        System.out.println("With date" + simple.format(result));

        System.out.println(msToHHmm(0));

    }
}
