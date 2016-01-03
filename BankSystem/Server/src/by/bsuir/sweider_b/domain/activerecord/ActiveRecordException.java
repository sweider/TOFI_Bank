package by.bsuir.sweider_b.domain.activerecord;

/**
 * Created by sweid on 06.12.2015.
 */
public class ActiveRecordException extends RuntimeException{

    public ActiveRecordException(Throwable cause) {
        super(cause);
    }

    public ActiveRecordException(String msg){
        super(msg);
    }

}