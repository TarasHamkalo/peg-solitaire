package dev.taras.hamkalo.spring.auth.oauth.config;

import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dev.taras.hamkalo.spring.auth.oauth.config.util.JwkSetGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenConfig {

  private static final String GAME_CLAIM = "pegsolitaire";

  @Value("${oauth.client.trusted.id}")
  String trustedClient;

  @Bean
  OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer() {
    return context -> {
      var clientId = context.getRegisteredClient().getClientId();
      if (!trustedClient.equals(clientId)) {
        var authorities = context.getPrincipal().getAuthorities().stream()
          .map(GrantedAuthority::getAuthority)
          .toList();

        context.getClaims().claim("username", context.getPrincipal().getName());
        context.getClaims().claim("authorities", authorities);
        context.getClaims().claim("game", GAME_CLAIM);
      }
    };
  }

  @Bean
  JWKSource<SecurityContext> jwkSource() {
    return new ImmutableJWKSet<>(JwkSetGenerator.generateJwkSet());
  }

  @Bean
  JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
    return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
  }

}
