package by.bsuir.sweider_b.domain.files;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import by.bsuir.sweider_b.domain.creditapplication.CreditApplication;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by sweid on 08.12.2015.
 */
@Entity
@Table(name = "uploaded_files")
public class UploadedFile extends ActiveRecord {

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "created_at")
    private Date createtAt;

    @Column(name = "file_data")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "credit_application_id")
    private CreditApplication creditApplication;


}
