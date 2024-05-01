package sk.tuke.gamestudio.server.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import sk.tuke.gamestudio.server.security.token.converter.UsernameAuthoritiesJwtTokenConverter;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

  @Value("${jwk.set.uri}")
  String jwkSetUri;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .oauth2ResourceServer(resourceServer -> resourceServer
        .jwt(jwtConfigurer -> jwtConfigurer
          .jwtAuthenticationConverter(converter())
          .jwkSetUri(jwkSetUri)))
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers(HttpMethod.GET, "/api/comments/*").hasAnyRole("USER", "MANAGER")
        .requestMatchers(HttpMethod.GET, "/api/scores/*").hasAnyRole("USER", "MANAGER")
        .requestMatchers(HttpMethod.GET, "/api/ratings/**").hasAnyRole("USER", "MANAGER")
        .requestMatchers(HttpMethod.POST, "/api/comments/*").hasRole("USER")
        .requestMatchers(HttpMethod.POST, "/api/ratings").hasRole("USER")
        .requestMatchers(HttpMethod.POST, "/api/scores").hasRole("MANAGER")
        .anyRequest().authenticated())
      .build();
  }

  /*
    A lot of cors configuration will be added.....
   */

  @Bean
  UsernameAuthoritiesJwtTokenConverter converter() {
    return new UsernameAuthoritiesJwtTokenConverter();
  }

}
