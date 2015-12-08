package by.bsuir.sweider_b.domain.files;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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


}
