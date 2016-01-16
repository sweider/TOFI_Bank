package by.bsuir.sweider_b.banksystem.server.domain.employee;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.creditapplication.Review;
import by.bsuir.sweider_b.banksystem.server.domain.user.UserCredentials;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "employees")
public class Employee extends ActiveRecord {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @OneToMany(mappedBy = "reviewCreator")
    private List<Review> reviews;

    private Employee(){}

    public Employee(String login, String password, EmployeeRole role){
        this.userCredentials = new UserCredentials(login, password);
        this.role = role;
    }

    public String getLogin(){
        return this.userCredentials.getLogin();
    }

    public String getPassword(){
        return this.userCredentials.getPassword();
    }

    public static Filter<Employee> filter(){
        return ActiveRecord.filter(Employee.class);
    }
}
