package by.bsuir.sweider_b.domain.activerecord;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Created by sweid on 06.12.2015.
 */
@Component
public class SpringContextRefreshedListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(SpringContextRefreshedListener.class);

    @EventListener
    void contextRefreshedEvent(ContextRefreshedEvent contextEvent) {
        ApplicationContext ctx = contextEvent.getApplicationContext();
        try {
            this.injectSessionFactoryToActiveRecord(ctx);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            LOGGER.error("Failed to set session factory to active record.", ex);
        }
    }

    private void injectSessionFactoryToActiveRecord(ApplicationContext ctx) throws NoSuchFieldException, IllegalAccessException, BeansException, IllegalArgumentException, SecurityException {
        SessionFactory sf = ctx.getBean(SessionFactory.class);
        synchronized (ActiveRecord.class){
            Field field = ActiveRecord.class.getDeclaredField("sessionFactory");
            field.setAccessible(true);
            field.set(null, sf);
            field.setAccessible(false);
        }
    }
}