package by.bsuir.sweider_b.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by sweid on 06.12.2015.
 */
@Configuration
@Import(HibernateConfig.class)
@ComponentScan({"by.bsuir.sweider_b"})
public class AppConfig {
}
