package by.bsuir.sweider_b.banksystem.server.domain.customer;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "passports_data")
public class PassportData extends ActiveRecord {


    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "lastname")
    private String lastname;

    private PassportData(){}

    public PassportData(String passportNumber, String name, String surname, String lastname) {
        this.passportNumber = passportNumber;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
