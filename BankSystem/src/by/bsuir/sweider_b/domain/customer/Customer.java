package by.bsuir.sweider_b.domain.customer;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.user.UserCredentials;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "customers")
public class Customer extends ActiveRecord{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(mappedBy = "user_credentials_id")
    private UserCredentials userCredentials;

    @OneToOne(mappedBy = "customer_info_id")
    private CustomerInfo customerInfo;


    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    @Override
    protected void setIdAfterSave(int id) {
        this.id = id;
    }



    public static Filter<Customer> filter(){
        return ActiveRecord.filter(Customer.class);
    }
}
