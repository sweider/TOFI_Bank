package by.bsuir.sweider_b.banksystem.shared.services.authentication;

/**
 * Created by sweid on 06.12.2015.
 */
public class AuthenticationException extends Exception {
    public AuthenticationException(){
        super("Недействительная пара 'Логин':'Пароль'");
    }
}
