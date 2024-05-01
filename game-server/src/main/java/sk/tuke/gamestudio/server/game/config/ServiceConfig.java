package sk.tuke.gamestudio.server.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ServiceConfig {

  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
