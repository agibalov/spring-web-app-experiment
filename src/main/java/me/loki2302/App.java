package me.loki2302;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ MvcConfiguration.class, SecurityConfiguration.class })
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
