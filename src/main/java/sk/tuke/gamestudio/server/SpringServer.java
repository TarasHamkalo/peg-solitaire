package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EntityScan("sk.tuke.gamestudio.commons.entity")
@ComponentScan(
    basePackages = {
        "sk.tuke.gamestudio.commons.service.impl.jpa",
        "sk.tuke.gamestudio.server"
    }
)
public class SpringServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class, args);
    }
}
