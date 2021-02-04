/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

import java.sql.SQLException;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author CORI
 */
public class CompanyAccessService {

    private final DBEmployeeAccess db;
    public static final long REQUIRED_WORKING_HOURS = 8 * 3600000; // in ms

    public CompanyAccessService() throws ClassNotFoundException, SQLException {
        db = new DBEmployeeAccess();
    }

    public synchronized String handleEmployeeAccess(String employeeId) throws SQLException {
        //employeeId = employeeId.toUpperCase();
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e != null) {
            long currentTime = System.currentTimeMillis();
            String currentDate = UtileClass.getCurrentDate();
            if (db.findPresenceByEmployeeId(employeeId)) {
                long exitTime = currentTime;
                long entryTime = db.findEntryTimeByEmployeeAccessIdAndDate(employeeId, UtileClass.getCurrentDate());
                long workedHours = computeWorkedHours(entryTime, exitTime);
                db.updateAtExitByEmployeeAccessIdAndDate(employeeId, currentDate, exitTime, workedHours, computeBankOfHours(workedHours));
                db.updatePresenceByEmployeeId(employeeId, false);
                return "Employee " + employeeId + " left the building at " + UtileClass.msToHHmm(exitTime) + "\n";
            } else {
                EmployeeAccessEntity ea = new EmployeeAccessEntity(employeeId, currentTime, currentDate);
                db.insertEmployeeAccess(ea);
                db.updatePresenceByEmployeeId(employeeId, true);
                return "Employee " + employeeId + " entered in building at " + UtileClass.msToHHmm(currentTime) + "\n";
            }

        } else {
            return "Employee " + employeeId + " does not exist";
        }
    }

    public synchronized String addNewEmployee(String employeeId, String nume, String surname) throws SQLException {
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e == null) {
            EmployeeEntity newE = new EmployeeEntity(employeeId, nume, surname);
            db.insertEmployee(newE);
            return newE.toString();
        } else {
            return "Employee " + employeeId + " already exist";
        }
    }

    public synchronized String deleteEmployee(String employeeId) throws SQLException {
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e != null) {
            db.deleteByEmployeeAccessId(employeeId);
            db.deleteByEmployeeId(employeeId);
            return "Employee " + employeeId + " DELETED";
        } else {
            return "Employee " + employeeId + " does NOT exist";
        }
    }

    public synchronized String seeAllEmployees() throws SQLException {
        return db.selectEmployee();
    }

    public synchronized String seeWorkedHourPerMonth(String employeeId, int month) throws SQLException {
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e != null) {
            return db.getEmployeeAccessByEmployeeAccessIdAndMonth(employeeId, month);
        } else {
            return "**********************************\n "
                    + " " + employeeId + " does NOT exist "
                    + "\n**********************************";
        }
    }

    public synchronized String seeDataFromCurrentYearForAnEmployee(String employeeId) throws SQLException {
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e != null) {
            return db.getEmployeeAccessByEmployeeAccessIdForCurrentYear(employeeId);
        } else {
            return "**********************************\n "
                    + " " + employeeId + " does NOT exist "
                    + "\n**********************************";
        }
    }

    public synchronized String seeMontlyOvertimForAnEmployee(String employeeId, int month) throws SQLException {
        EmployeeEntity e = db.findByEmployeeId(employeeId);
        if (e != null) {
            return db.sumBankOfHoursByEmployeeAccessIdAndMonth(employeeId, month);
        } else {
            return "**********************************\n "
                    + " " + employeeId + " does NOT exist "
                    + "\n**********************************";
        }
    }

    private long computeWorkedHours(long entryTime, long exitTime) {
        System.out.println(DurationFormatUtils.formatDuration(exitTime - entryTime, "HH:mm"));
        return (exitTime - entryTime);
    }

    private long computeBankOfHours(long workedHours) {
        return workedHours - REQUIRED_WORKING_HOURS;
    }

    public static void main(String[] args) throws Exception {
        CompanyAccessService c = new CompanyAccessService();
        System.out.println(c.handleEmployeeAccess("F93439C"));
        Thread.sleep(2000);
        System.out.println(259755697);
        UtileClass.computeDuration(259755697);

        //var = ( 143503510 );
        System.out.println(UtileClass.computeDuration(259755697));
    }
}
