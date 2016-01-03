package by.bsuir.sweider_b.domain.customer;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.creditapplication.CreditApplication;
import by.bsuir.sweider_b.domain.user.UserCredentials;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers")
public class Customer extends ActiveRecord {

    @OneToOne
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @OneToOne
    @JoinColumn(name = "customer_info_id")
    private CustomerInfo customerInfo;

    @OneToMany(mappedBy = "customer")
    private List<CreditApplication> creditApplications;


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
