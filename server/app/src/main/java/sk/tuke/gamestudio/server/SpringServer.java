package sk.tuke.gamestudio.server;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EntityScan("sk.tuke.gamestudio.data.entity")
@ComponentScan(
    basePackages = {
        "sk.tuke.gamestudio.data.service.jpa",
        "sk.tuke.gamestudio.server"
    }
)
public class SpringServer {
    public static void main(String[] args) {
        SpringApplication.run(SpringServer.class, args);
    }

    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }
}
