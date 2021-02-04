/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author CORI
 */
public class EmployeeAccessEntity {

    private long accessId;
    private String employeeId;
    private long entryTime;
    private long exitTime;
    private long workedHours;
    private long bankOfHours;
    private String workingDay;

    public EmployeeAccessEntity(String employeeId, long entryTime, String workingDay) {
        this.employeeId = employeeId;
        this.entryTime = entryTime;
        this.exitTime = 0;
        this.workedHours = 0;
        this.bankOfHours = 0;
        this.workingDay = workingDay;
    }

    public EmployeeAccessEntity(String userId, long entryTime, long exitTime, long workedHours, long bankOfHours, String workingDay) {
        this.employeeId = userId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.workedHours = workedHours;
        this.bankOfHours = bankOfHours;
        this.workingDay = workingDay;
    }

    public void setAccessId(long accessId) {
        this.accessId = accessId;
    }

    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    public void setBankOfHours(long bankOfHours) {
        this.bankOfHours = bankOfHours;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public long getAccessId() {
        return accessId;
    }

    public long getExitTime() {
        return exitTime;
    }

    public long getBankOfHours() {
        return bankOfHours;
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    public void setWorkedHours(long workedHours) {
        this.workedHours = workedHours;
    }

    public long getWorkedHours() {
        return workedHours;
    }

    @Override
    public String toString() {
        return "EmployeeAccessEntity{" + "employeeId=" + employeeId
                + ", entryTime=" + UtileClass.msToHHmm(entryTime) + ", exitTime="
                + UtileClass.msToHHmm(exitTime) + ", workedHours=" + UtileClass.computeDuration(workedHours)
                + ", bankOfHours=" + UtileClass.computeDuration(bankOfHours) + ", workingDay=" + workingDay + '}';
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
//        EmployeeAccessEntity employee = new EmployeeAccessEntity("f9343c", 0, "2020-02-01");
//        employee.getCurrentDate(); 
//        
//        System.out.println(employee.toString());
        long yourmilliseconds = 222659789;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        System.out.println(sdf.format(resultdate));
        Thread.sleep(4000);
        long timeInMS = System.currentTimeMillis() - yourmilliseconds;
        System.out.println(DurationFormatUtils.formatDuration(timeInMS, "HH:mm:ss,SSS"));

        String line = "F93439C+CORINA+dAVID";
        StringTokenizer st = new StringTokenizer(line, "+");
        String oprnd1 = st.nextToken();
        System.out.println(oprnd1);
        String operation = st.nextToken();
        System.out.println(operation);
        String SURNAME = st.nextToken();
        System.out.println(SURNAME);
    }
}
