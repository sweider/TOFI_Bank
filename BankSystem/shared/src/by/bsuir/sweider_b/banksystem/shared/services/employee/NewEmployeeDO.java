package by.bsuir.sweider_b.banksystem.shared.services.employee;

import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Created by sweid on 16.01.2016.
 */
public class NewEmployeeDO implements Serializable{

    private final String login;
    private final String password;
    private final EmployeeRole role;

    private final String name;
    private final String lastName;
    private final String surName;
    private final String passportNbmr;

    public NewEmployeeDO(String login, String password, EmployeeRole role, String passportNumber, String name, String surname, String lastname) {
        this.passportNbmr = passportNumber;
        this.name = name;
        this.surName = surname;
        this.lastName = lastname;
        this.login = login;
        this.password = password;
        this.role = role;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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

    public String getPassportNbmr() {
        return passportNbmr;
    }
}
