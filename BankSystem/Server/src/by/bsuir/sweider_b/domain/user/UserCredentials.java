package by.bsuir.sweider_b.domain.user;

import by.bsuir.sweider_b.domain.activerecord.ActiveRecord;
import org.hibernate.criterion.Criterion;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * Created by sweid on 06.12.2015.
 */
@Entity
@Table(name = "user_credentials")
public class UserCredentials extends ActiveRecord {

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;


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

    public static Optional<UserCredentials> find(int id) {
        return ActiveRecord.find(UserCredentials.class, id);
    }

    public static Optional<UserCredentials> first(Criterion... restrictions) {
        return ActiveRecord.first(UserCredentials.class, restrictions);
    }

    public static Filter<UserCredentials> filter() {
        return ActiveRecord.filter(UserCredentials.class);
    }

    protected static List<UserCredentials> all(Criterion... restrictions) {
        return ActiveRecord.all(UserCredentials.class, restrictions);
    }
}
