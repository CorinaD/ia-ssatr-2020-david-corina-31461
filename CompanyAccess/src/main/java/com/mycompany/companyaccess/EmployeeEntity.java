/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.companyaccess;

/**
 *
 * @author CORI
 */
public class EmployeeEntity {

    private String employeeId;
    private String name;
    private String surname;
    private Boolean presence;

    public EmployeeEntity(String employeeId, String name, String surname) {
        this.employeeId = employeeId;
        this.name = name;
        this.surname = surname;
        this.presence = false;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "EmployeeEntity{" + "employeeId=" + employeeId + ", name=" + name + ", surname=" + surname + '}';
    }
}
