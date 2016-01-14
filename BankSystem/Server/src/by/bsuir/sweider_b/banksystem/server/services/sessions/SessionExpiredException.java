package by.bsuir.sweider_b.banksystem.server.services.sessions;

/**
 * Created by sweid on 06.12.2015.
 */
public class SessionExpiredException extends Exception {
    public SessionExpiredException(){
        super("Время действия сессии истекло");
    }
}
