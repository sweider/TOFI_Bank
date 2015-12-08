package by.bsuir.sweider_b.domain.creditapplication;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.employee.Employee;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "reviews")
public class Review extends ActiveRecord{
    @Column(name = "date")
    @Type(type = "timestamp")
    private Timestamp reviewDate;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "review_creator_id")
    private Employee reviewCreator;
}
