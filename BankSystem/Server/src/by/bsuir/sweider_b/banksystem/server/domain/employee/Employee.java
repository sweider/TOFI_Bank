package by.bsuir.sweider_b.banksystem.server.domain.employee;

import by.bsuir.sweider_b.banksystem.server.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.banksystem.server.domain.creditapplication.Review;
import by.bsuir.sweider_b.banksystem.server.domain.customer.PassportData;
import by.bsuir.sweider_b.banksystem.server.domain.user.UserCredentials;
import by.bsuir.sweider_b.banksystem.shared.model.EmployeeRole;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "employees")
public class Employee extends ActiveRecord {
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "passport_data_id")
    private PassportData passportData;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private EmployeeRole role;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "reviewCreator")
    private List<Review> reviews;

    private Employee(){}

    public Employee(String login, String password, EmployeeRole role, String passportNmb, String name, String lastName, String surname){
        this.userCredentials = new UserCredentials(login, password);
        this.passportData = new PassportData(passportNmb, name,lastName,surname);
        this.role = role;
        this.isActive = true;
    }

    public String getLogin(){
        return this.userCredentials.getLogin();
    }

    public String getPassword(){
        return this.userCredentials.getPassword();
    }

    public EmployeeRole getRole(){ return this.role; }

    public PassportData getPassportData(){
        return  this.passportData;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public static Filter<Employee> filter(){
        return ActiveRecord.filter(Employee.class);
    }

    public static Optional<Employee> find(Integer id){
        return ActiveRecord.find(Employee.class, id);
    }
}
