package by.bsuir.sweider_b.banksystem.shared.model;

import java.io.Serializable;

/**
 * Created by sweid on 08.12.2015.
 */
public enum EmployeeRole implements Serializable{
    OPERATOR("Оператор"),
    SECURITY("Сотрудник охраны"),
    CREDIT_COMMISSION("Член кредитной комиссии"),
    ADMIN("Администратор");

    private final String userFriendlyName;

    private EmployeeRole(String ufName){
        this.userFriendlyName = ufName;
    }
    public String getUserFriendlyName(){
        return this.userFriendlyName;
    }
}
