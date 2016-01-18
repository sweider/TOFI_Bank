package by.bsuir.sweider_b.banksystem.shared.services.customers;

import java.io.Serializable;

/**
 * Created by sweid on 18.01.2016.
 */
public class CustomerDO implements Serializable {
    private int id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String surName;
    private String passportNumber;
    private String city;
    private String street;
    private String building;
    private String room;
    private String phoneNumber;

    public CustomerDO(String login, String password, String name, String lastName, String surName, String passportNumber,
                      String city, String street, String building, String room,
                      String phoneNumber) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.surName = surName;
        this.passportNumber = passportNumber;
        this.city = city;
        this.street = street;
        this.building = building;
        this.room = room;
        this.phoneNumber = phoneNumber;
    }

    public CustomerDO(int id, String name, String lastName, String surName, String passportNumber, String city, String street, String building, String room, String phone) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.surName = surName;
        this.passportNumber = passportNumber;
        this.city = city;
        this.street = street;
        this.building = building;
        this.room = room;
        this.phoneNumber = phone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoom() {
        return room;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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

    public String getPassportNumber() {
        return passportNumber;
    }
}
