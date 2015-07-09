package usersmanagement;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application /*extends SpringBootServletInitializer*/ {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .showBanner(false)
                .run(args);
    }


}
