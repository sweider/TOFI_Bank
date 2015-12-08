package by.bsuir.sweider_b.domain.customer;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.creditapplication.CreditApplication;
import by.bsuir.sweider_b.domain.user.UserCredentials;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers")
public class Customer extends ActiveRecord {


    @OneToOne(mappedBy = "user_credentials_id")
    private UserCredentials userCredentials;

    @OneToOne(mappedBy = "customer_info_id")
    private CustomerInfo customerInfo;

    @OneToMany(mappedBy = "CreditApplication")
    private ArrayList<CreditApplication> creditApplications;



    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public static Filter<Customer> filter() {
        return ActiveRecord.filter(Customer.class);
    }
}
