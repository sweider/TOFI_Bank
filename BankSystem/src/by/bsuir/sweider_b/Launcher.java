package by.bsuir.sweider_b;
import by.bsuir.sweider_b.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by sweid on 06.12.2015.
 */
public class Launcher {
    public static void main(String[] args){
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);
    }
}
