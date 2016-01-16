package by.bsuir.sweider_b.banksystem.shared.services.employee;

import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;

import java.io.Serializable;

/**
 * Created by sweid on 16.01.2016.
 */
public class NewEmployeeData implements Serializable{
    private final String login;
    private final String password;
    private final EmployeeRole role;

    public NewEmployeeData(String login, String password, EmployeeRole role) {

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


}
