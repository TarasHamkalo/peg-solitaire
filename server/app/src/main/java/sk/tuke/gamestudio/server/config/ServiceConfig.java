package sk.tuke.gamestudio.server.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackages = {
        "sk.tuke.gamestudio.data.service.jpa",
        "sk.tuke.gamestudio.server"
    }
)
@EntityScan("sk.tuke.gamestudio.data.entity")
public class ServiceConfig {

    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }

}
