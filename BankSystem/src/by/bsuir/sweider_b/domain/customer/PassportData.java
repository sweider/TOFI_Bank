package by.bsuir.sweider_b.domain.customer;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "passports_data")
public class PassportData extends ActiveRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "passportNumber")
    private String passportNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "lastname")
    private String lastname;

    public int getId(){
        return this.id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    protected void setIdAfterSave(int id) {
        this.id = id;
    }
}
