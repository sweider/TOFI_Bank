package by.bsuir.sweider_b.banksystem.server.domain.creditapplication;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.credit.CreditKind;
import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.server.domain.files.UploadedFile;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "credit_applications")
public class CreditApplication extends ActiveRecord {

    @Column(name = "application_sum")
    private long applicationSum;

    @Column(name = "customers_income")
    private long customersIncome;

    @ManyToOne
    @JoinColumn(name = "credit_kind_id")
    private CreditKind creditKind;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CreditApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @OneToMany(mappedBy = "creditApplication")
    private List<UploadedFile> attachedDocuments;

    private CreditApplication(){}

    public CreditApplication(long applicationSum, long income, CreditKind creditKind, Customer customer){
        this.applicationSum = applicationSum;
        this.customersIncome = income;
        this.creditKind = creditKind;
        this.customer = customer;
        this.status = CreditApplicationStatus.WAITING_FOR_SECURITY_DEPARTMENT_REVIEW;
    }

    public long getApplicationSum() {
        return applicationSum;
    }

    public long getCustomersIncome() {
        return customersIncome;
    }

    public CreditKind getCreditKind() {
        return creditKind;
    }

    public CreditApplicationStatus getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<UploadedFile> getAttachedDocuments() {
        return attachedDocuments;
    }

    public void setStatus(CreditApplicationStatus status) {
        this.status = status;
    }

    public static Optional<CreditApplication> find(int id){
        return ActiveRecord.find(CreditApplication.class, id);
    }

    public static Filter<CreditApplication> filter(){
        return ActiveRecord.filter(CreditApplication.class);
    }
}
