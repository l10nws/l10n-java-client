package ws.l10n.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@PropertySource(value = "classpath:application.properties")
@ImportResource(locations = {"classpath:/spring-l10n.xml"})
public class L10nApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(L10nApplication.class, args);
    }

}