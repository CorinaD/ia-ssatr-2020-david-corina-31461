/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.lang.time.DurationFormatUtils;

/**
 *
 * @author CORI
 */
public class DBEmployeeAccess {

    private Connection conn;

    public DBEmployeeAccess() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        conn = DriverManager.getConnection("jdbc:derby://localhost:1527/track_access;create=false", "app", "app");
    }

    public void insertEmployeeAccess(EmployeeAccessEntity e) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("INSERT INTO EMPLOYEEACCESS(employeeId,entryTime,workingDay) VALUES ('" + e.getEmployeeId() + "'," + e.getEntryTime() + ",'" + e.getWorkingDay() + "')");
        s.close();
    }

    public EmployeeAccessEntity findByEmployeeAccessId(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM EMPLOYEEACCESS WHERE employeeid ='" + employeeId + "' ORDER BY ACCESSID DESC FETCH FIRST ROW ONLY");
        if (rs.next()) {
            return new EmployeeAccessEntity(rs.getString("employeeid"), rs.getLong("entryTime"), rs.getLong("exitTime"),
                    rs.getLong("workedHours"), rs.getLong("bankOfHours"), rs.getString("workingDay"));
        } else {
            return null;
        }
    }

    public EmployeeAccessEntity findByEmployeeAccessIdAndDate(String employeeId, String workingDay) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM EMPLOYEEACCESS WHERE employeeid ='" + employeeId + "' AND workingDay='" + workingDay + "' ORDER BY ACCESSID DESC FETCH FIRST ROW ONLY");
        if (rs.next()) {
            return new EmployeeAccessEntity(rs.getString("employeeid"), rs.getLong("entryTime"), rs.getLong("exitTime"), rs.getLong("workedHours"), rs.getLong("bankOfHours"), rs.getString("workingDay"));
        } else {
            return null;
        }
    }

    public long findEntryTimeByEmployeeAccessIdAndDate(String employeeId, String workingDay) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT EMPLOYEEACCESS.ENTRYTIME FROM EMPLOYEEACCESS WHERE employeeid ='" + employeeId + "' AND workingDay='" + workingDay + "' ORDER BY ACCESSID DESC FETCH FIRST ROW ONLY");
        if (rs.next()) {
            return rs.getLong("entryTime");
        } else {
            return 0;
        }
    }

    public void deleteByEmployeeAccessId(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("DELETE FROM EMPLOYEEACCESS WHERE employeeid='" + employeeId + "'");
    }

    public long findAccessIdByEmployeeAccessId(String employeeId, String workingDay) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT EMPLOYEEACCESS.ACCESSID FROM EMPLOYEEACCESS WHERE employeeid ='" + employeeId + "' AND workingDay='" + workingDay + "'");
        if (rs.next()) {
            return rs.getLong("AccessId");
        } else {
            return 0;
        }
    }

    public void updateAtExit(long accessId, long exitTime, long workedHours, long bankOfHours) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("UPDATE EMPLOYEEACCESS SET EXITTIME=" + exitTime + ", WORKEDHOURS=" + workedHours + ", BANKOFHOURS=" + bankOfHours + " WHERE accessId =" + accessId + "");
    }

    public void updateAtExitByEmployeeAccessIdAndDate(String employeeId, String workingDay, long exitTime, long workedHours, long bankOfHours) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("UPDATE EMPLOYEEACCESS SET EXITTIME=" + exitTime + ", WORKEDHOURS=" + workedHours + ", BANKOFHOURS=" + bankOfHours + " WHERE employeeid ='" + employeeId + "' AND workingDay='" + workingDay + "'");
    }

    public String getEmployeeAccessByEmployeeAccessIdAndMonth(String employeeId, int month) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT EMPLOYEEACCESS.EMPLOYEEID, EMPLOYEE.NAME, EMPLOYEE.SURNAME, EMPLOYEEACCESS.WORKINGDAY, EMPLOYEEACCESS.WORKEDHOURS \n"
                + "FROM EMPLOYEE INNER JOIN\n"
                + "                      EMPLOYEEACCESS ON EMPLOYEEACCESS.EMPLOYEEID = EMPLOYEE.EMPLOYEEID\n"
                + "WHERE EMPLOYEEACCESS.EMPLOYEEID = '" + employeeId + "' and MONTH(WORKINGDAY) = " + month + " ORDER BY WORKINGDAY, ENTRYTIME");
        String output = "ID        \t Name      \t Surname \t  Day  \t Hours  \n";
        while (rs.next()) {
            output += rs.getString("EMPLOYEEID") + "        \t " + rs.getString("NAME")
                    + "      \t " + rs.getString("SURNAME") + " \t  " + rs.getString("WORKINGDAY")
                    + "   \t " + UtileClass.computeDuration(rs.getLong("WORKEDHOURS")) + "  \n";
        }

        return output;
    }

    public String getEmployeeAccessByEmployeeAccessIdForCurrentYear(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT EMPLOYEEACCESS.EMPLOYEEID, EMPLOYEE.NAME, EMPLOYEE.SURNAME, EMPLOYEEACCESS.WORKINGDAY, EMPLOYEEACCESS.ENTRYTIME, EMPLOYEEACCESS.EXITTIME, EMPLOYEEACCESS.WORKEDHOURS, EMPLOYEEACCESS.BANKOFHOURS \n"
                + "FROM EMPLOYEE INNER JOIN\n"
                + "EMPLOYEEACCESS ON EMPLOYEEACCESS.EMPLOYEEID = EMPLOYEE.EMPLOYEEID\n"
                + "WHERE EMPLOYEEACCESS.EMPLOYEEID = '" + employeeId + "' and YEAR(WORKINGDAY) = YEAR(CURRENT_DATE) ORDER BY WORKINGDAY, ENTRYTIME");
        String output = "ID        \t Name      \t Surname \t  Day   \t Entry Time \t Exit Time \t Worked Hours \t Bank of Hours \n";
        while (rs.next()) {
            output += rs.getString("EMPLOYEEID") + "        \t " + rs.getString("NAME") + "      \t " + rs.getString("SURNAME") + " \t  " + rs.getString("WORKINGDAY") + "     \t "
                    + UtileClass.msToHHmm(rs.getLong("ENTRYTIME")) + "        \t " + UtileClass.msToHHmm(rs.getLong("EXITTIME")) + "  \t "
                    + UtileClass.computeDuration(rs.getLong("WORKEDHOURS")) + "  \t "
                    + UtileClass.computeDuration(rs.getLong("BANKOFHOURS")) + "  \n";
        }

        return output;
    }

    //SEE OVERTIME 
    public String sumBankOfHoursByEmployeeAccessIdAndMonth(String employeeId, int month) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT SUM (BANKOFHOURS) AS OVERTIME\n"
                + "FROM EMPLOYEEACCESS \n"
                + "WHERE EMPLOYEEID='" + employeeId + "' AND MONTH(WORKINGDAY) = " + month);

        String output = "ID        \t Overtime \n";
        while (rs.next()) {
            output += employeeId + "        \t "
                    + UtileClass.computeDuration((rs.getLong("OVERTIME"))) + "  \n";
        }

        return output;
    }

    //for employee
    public void insertEmployee(EmployeeEntity e) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("INSERT INTO EMPLOYEE VALUES ('" + e.getEmployeeId() + "','" + e.getName() + "','" + e.getSurname() + "'," + e.getPresence() + ")");
        s.close();
    }

    public String selectEmployee() throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM APP.EMPLOYEE");
        String result = "ID        \t Name \t Surname \n";
        while (rs.next()) {
            result += "" + rs.getString("employeeid") + " \t" + rs.getString("name") + " \t" + rs.getString("surname") + "\n";
        }
        return result;
    }

    public EmployeeEntity findByEmployeeId(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM EMPLOYEE WHERE employeeid ='" + employeeId + "'");
        if (rs.next()) {
            return new EmployeeEntity(rs.getString("employeeid"), rs.getString("name"), rs.getString("surname"));
        } else {
            return null;
        }
    }

    public void deleteByEmployeeId(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("DELETE FROM EMPLOYEE WHERE employeeid='" + employeeId + "'");
    }

    public Boolean findPresenceByEmployeeId(String employeeId) throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT EMPLOYEE.PRESENCE FROM EMPLOYEE WHERE employeeid ='" + employeeId + "'");
        if (rs.next()) {
            return rs.getBoolean("presence");
        } else {
            return false;
        }
    }

    public void updatePresenceByEmployeeId(String employeeId, Boolean presence) throws SQLException {
        Statement s = conn.createStatement();
        s.executeUpdate("UPDATE EMPLOYEE SET PRESENCE='" + presence + "' WHERE employeeid ='" + employeeId + "'");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        DBEmployeeAccess db = new DBEmployeeAccess();
//        long t = System.currentTimeMillis();
//        //db.insertEmployeeAccess(new EmployeeAccessEntity("F9322C",t,"2020-02-01"));
//        EmployeeAccessEntity result = db.findByEmployeeAccessIdAndDate("F93439C", "2020-05-12");
//        System.out.println(result);
//        
//        if (result != null) {
//            System.out.println("AM GASIT USERUL");
//        } else {
//            System.out.println("Entry not found!");
//        }
//        
//        long val = db.findAccessIdByEmployeeAccessId("F93439C", "2020-05-12");
//        System.out.println(val);
//        t = System.currentTimeMillis();
//        db.updateAtExit(val, t, 8, 0);
//
//        result = db.findByEmployeeAccessIdAndDate("F93439C", "2020-05-12");
//        System.out.println(result);
//        if (result != null) {
//            System.out.println("AM GASIT USERUL");
//        } else {
//            System.out.println("Entry not found!");
//        }
//
//        System.out.println(db.selectEmployee());
//
//        System.out.println(db.getEmployeeAccessByEmployeeAccessIdAndMonth("F93439C", 2));
//
//       System.out.println(db.getEmployeeAccessByEmployeeAccessIdForCurrentYear("F93439C"));
//
//        System.out.println(db.sumBankOfHoursByEmployeeAccessIdAndMonth("F93439C", 2));

//    db.updatePresenceByEmployeeAccessId("F93439C", true);
//    Boolean resultBool = db.findPresenceByEmployeeAccessId("F93439C");
//    if(true == resultBool){
//        System.out.println("This user is present");
//        }else{
//            System.out.println("Entry not found!");
//        } 
//    }
//        long entryTime = System.currentTimeMillis();
//
//        System.out.println("Good in time " + UtileClass.convertMstoActualDate(entryTime ));
//
//        System.out.println("on millies " + entryTime);
//        long exitTime = entryTime + 9 * 3600000;
//
//        System.out.println("exit in time " + UtileClass.msToHHmm(exitTime));
//
//        System.out.println("on millies " + exitTime);
//        System.out.println("DIFF " + (exitTime - entryTime));
//        System.out.println(DurationFormatUtils.formatDuration(exitTime - entryTime, "HH:mm"));
//
//        String employeeId = "F93415C";
//        String date = "2021-02-04";
//        //db.updatePresenceByEmployeeId(employeeId, true);
//        long entryTime1 = db.findEntryTimeByEmployeeAccessIdAndDate(employeeId, date);
//        long exitTime = entryTime1 + 10 * 3600000;
//        long workedHours = exitTime - entryTime1;
//        db.updateAtExitByEmployeeAccessIdAndDate(employeeId, date, exitTime, workedHours, ( workedHours - 8 * 3600000));
//        db.updatePresenceByEmployeeId(employeeId, false);
    }
}
