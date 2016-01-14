package by.bsuir.sweider_b.banksystem.server.domain.employee;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.creditapplication.Review;
import by.bsuir.sweider_b.banksystem.server.domain.user.UserCredentials;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "employees")
public class Employee extends ActiveRecord {
    @OneToOne
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @OneToMany(mappedBy = "reviewCreator")
    private List<Review> reviews;

    public static Filter<Employee> filter(){
        return ActiveRecord.filter(Employee.class);
    }
}
