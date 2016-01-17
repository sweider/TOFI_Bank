package by.bsuir.sweider_b.banksystem.shared.services.employee;

import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;

import java.io.Serializable;

/**
 * Created by sweid on 17.01.2016.
 */
public class EmployeeShowDO implements Serializable{
    private final int id;
    private final String login;
    private final EmployeeRole role;

    private final String name;
    private final String lastName;
    private final String surName;

    private final String passportData;

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSurName() {
        return surName;
    }

    public String getPassportNmbr() {
        return passportData;
    }

    public EmployeeShowDO(int id, String login, EmployeeRole role, String name, String lastName, String surName, String pasportData) {

        this.id = id;
        this.login = login;
        this.role = role;
        this.name = name;
        this.lastName = lastName;
        this.surName = surName;
        this.passportData = pasportData;

    }
}
