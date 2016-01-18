package by.bsuir.sweider_b.banksystem.server.domain.customer;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.creditapplication.CreditApplication;
import by.bsuir.sweider_b.banksystem.server.domain.user.UserCredentials;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers")
public class Customer extends ActiveRecord {

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_info_id")
    private CustomerInfo customerInfo;



    @OneToMany(mappedBy = "customer")
    private List<CreditApplication> creditApplications;

    @Column(name = "is_active")
    private boolean isActive;

    private Customer(){

    }

    public Customer(String passportNumber, String name, String surName, String lastName, String login, String pwd,
                    String city, String street, String building, String room,
                    String phoneNumber){
        this.userCredentials = new UserCredentials(login, pwd);
        this.customerInfo = new CustomerInfo(name, surName, lastName, passportNumber, city,street,building,room, phoneNumber);
        this.setActive(true);
    }

    public boolean isActive(){
        return this.isActive;
    }

    public void setActive(boolean value){
        this.isActive = value;
    }


    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public static Filter<Customer> filter() {
        return ActiveRecord.filter(Customer.class);
    }

    public static Optional<Customer> find(int id){
        return ActiveRecord.find(Customer.class, id);
    }
}
