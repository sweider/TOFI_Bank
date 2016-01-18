package by.bsuir.sweider_b.banksystem.server.domain.customer;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers_info")
public class CustomerInfo extends ActiveRecord {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_data_id")
    private PassportData passportData;

    @Column(name = "phone_number")
    private String phoneNumber;

    private CustomerInfo(){}

    public CustomerInfo(String name, String surName, String lastName, String passportNumber,
                        String city, String street, String building, String room,
                        String phone){
        this.passportData = new PassportData(passportNumber, name, surName, lastName);
        this.address = new Address(city, street, building, room);
        this.phoneNumber = phone;
    }

    public Address getAddress() {
        return address;
    }

    public PassportData getPassportData() {
        return passportData;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
