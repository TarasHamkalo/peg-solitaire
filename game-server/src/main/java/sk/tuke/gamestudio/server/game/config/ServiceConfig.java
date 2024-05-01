package sk.tuke.gamestudio.server.game.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.server.game.interceptors.Oauth2TokenAuthorizationInterceptor;

@Configuration
public class ServiceConfig {

  @Bean
  RestTemplate restTemplate(Oauth2TokenAuthorizationInterceptor interceptor) {
    return new RestTemplateBuilder()
      .additionalInterceptors(interceptor)
      .build();
  }

}
