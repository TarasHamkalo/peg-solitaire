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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sk.tuke.gamestudio.server.security.token.converter.UsernameAuthoritiesJwtTokenConverter;

import java.util.List;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

  @Value("${jwk.set.uri}")
  String jwkSetUri;

  @Value("${cors.allowed.host}")
  String allowedHost;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .cors(cors -> cors
        .configurationSource(corsConfigurationSource()))
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

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    var corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedOrigins(List.of(allowedHost));
    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "OPTIONS"));
    corsConfiguration.setAllowedHeaders(List.of("authorization"));
    corsConfiguration.setMaxAge(30L);

    corsConfiguration.setAllowCredentials(true);
    var corsConfigurationSource = new UrlBasedCorsConfigurationSource();
    corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

    return corsConfigurationSource;
  }


  @Bean
  UsernameAuthoritiesJwtTokenConverter converter() {
    return new UsernameAuthoritiesJwtTokenConverter();
  }

}
