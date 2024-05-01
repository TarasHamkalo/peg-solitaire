package sk.tuke.gamestudio.server.security.token.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import sk.tuke.gamestudio.server.security.token.authentication.UsernameJwtAuthenticationToken;

public class UsernameAuthoritiesJwtTokenConverter
  implements Converter<Jwt, UsernameJwtAuthenticationToken> {

  @Override
  public UsernameJwtAuthenticationToken convert(Jwt source) {
    var authorities = source.getClaimAsStringList("authorities").stream()
      .map(SimpleGrantedAuthority::new)
      .toList();

    return UsernameJwtAuthenticationToken.builder()
      .username(source.getClaim("username"))
      .authorities(authorities)
      .jwt(source)
      .build();
  }
}
