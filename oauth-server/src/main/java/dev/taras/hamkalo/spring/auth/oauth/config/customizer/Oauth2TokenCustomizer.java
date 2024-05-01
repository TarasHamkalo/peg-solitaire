package dev.taras.hamkalo.spring.auth.oauth.config.customizer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Oauth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

  @Value("${oauth.manager.scope.name}")
  String managerScope;

  @Value("${claims.game}")
  String gameClaim;

  @Override
  public void customize(JwtEncodingContext context) {
    var clientScopes = context.getRegisteredClient().getScopes();
    if (clientScopes.contains(managerScope)) {
      customizeManagerToken(context);
    } else {
      customizeUserToken(context);
    }
  }

  private void customizeManagerToken(JwtEncodingContext context) {
    context.getClaims().claim("authorities", List.of("ROLE_" + managerScope));
  }

  private void customizeUserToken(JwtEncodingContext context) {
    var authorities = context.getPrincipal().getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .toList();

    context.getClaims().claim("username", context.getPrincipal().getName());
    context.getClaims().claim("authorities", authorities);
    context.getClaims().claim("game", gameClaim);
  }

}
