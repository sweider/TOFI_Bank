package by.bsuir.sweider_b.domain.creditapplication;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.customer.Customer;

import javax.persistence.*;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "credit_applications")
public class CreditApplication extends ActiveRecord{

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CreditApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}