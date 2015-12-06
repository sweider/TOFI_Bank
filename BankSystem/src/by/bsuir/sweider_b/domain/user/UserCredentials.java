package by.bsuir.sweider_b.domain.user;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import org.hibernate.criterion.Criterion;

import javax.persistence.*;
import java.util.List;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "user_credentials")
public class UserCredentials extends ActiveRecord{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;


    public int getId(){
        return this.id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void setIdAfterSave(int id) {
        this.id = id;
    }

    public static UserCredentials find(int id){
        return ActiveRecord.find(UserCredentials.class,id);
    }

    public static UserCredentials first(Criterion... restrictions){
        return ActiveRecord.first(UserCredentials.class, restrictions);
    }

    public static Filter<UserCredentials> filter(){
        return ActiveRecord.filter(UserCredentials.class);
    }

    protected static final List<UserCredentials> all(Criterion ... restrictions) {
        return ActiveRecord.all(UserCredentials.class, restrictions);
    }
}
