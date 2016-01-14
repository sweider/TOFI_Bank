package by.bsuir.sweider_b.banksystem.server.domain.creditapplication;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.customer.Customer;
import by.bsuir.sweider_b.banksystem.server.domain.files.UploadedFile;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "credit_applications")
public class CreditApplication extends ActiveRecord {
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CreditApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "creditApplication")
    private List<UploadedFile> attachedDocuments;

}
