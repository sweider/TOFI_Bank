package by.bsuir.sweider_b.domain;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;

import javax.persistence.*;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "employees ")
public class Employee extends ActiveRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Override
    protected void setIdAfterSave(int id) {

    }
}
