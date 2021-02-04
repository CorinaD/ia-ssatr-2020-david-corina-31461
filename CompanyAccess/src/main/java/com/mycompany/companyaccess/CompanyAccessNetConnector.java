/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.StringTokenizer;

/**
 *
 * @author CORI
 */
public class CompanyAccessNetConnector {

    CompanyAccessService pService;

    public CompanyAccessNetConnector() throws ClassNotFoundException, SQLException {
        pService = new CompanyAccessService();
    }

    public void startServer() {

        try {

            ServerSocket ss = new ServerSocket(4050);

            while (true) {
                System.out.println("Astept conexiune de la client...");
                Socket s = ss.accept(); //metoda blocanta
                System.out.println("Clientul s-a conectat!");

                BufferedReader fluxIn = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter fluxOut = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);

                String line = fluxIn.readLine();
                int n = Integer.parseInt(line.substring(0, 1));
                System.out.print(n);
                line = line.substring(1);
                switch (n) {
                    case 1:
                        String employeeId = line;
                        String result = pService.handleEmployeeAccess(employeeId);
                        fluxOut.println(result);
                        break;
                    case 2:
                        String result2 = pService.seeAllEmployees();
                        fluxOut.println(result2);
                        fluxOut.println("done");
                        break;
                    case 3:
                        StringTokenizer st = new StringTokenizer(line, "+");
                        String id = st.nextToken();
                        String name = st.nextToken();
                        String surname = st.nextToken();
                        String result3 = pService.addNewEmployee(id, name, surname);
                        fluxOut.println(result3);
                        break;
                    case 4:
                        st = new StringTokenizer(line, "+");
                        id = st.nextToken();
                        int month = Integer.parseInt(st.nextToken());
                        String result4 = pService.seeWorkedHourPerMonth(id, month);
                        fluxOut.println(result4);
                        fluxOut.println("done");
                        break;
                    case 5:
                        id = line;
                        String result5 = pService.seeDataFromCurrentYearForAnEmployee(id);
                        fluxOut.println(result5);
                        fluxOut.println("done");
                        break;
                    case 6:
                        st = new StringTokenizer(line, "+");
                        id = st.nextToken();
                        month = Integer.parseInt(st.nextToken());
                        String result6 = pService.seeMontlyOvertimForAnEmployee(id, month);
                        fluxOut.println(result6);
                        fluxOut.println("done");
                        break;
                    case 7:
                        String result7 = pService.deleteEmployee(line);
                        fluxOut.println(result7);
                        break;

                    default:
                        System.err.println("Invalid data from client!");
                }
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CompanyAccessNetConnector netCon = new CompanyAccessNetConnector();
        netCon.startServer();
    }
}
