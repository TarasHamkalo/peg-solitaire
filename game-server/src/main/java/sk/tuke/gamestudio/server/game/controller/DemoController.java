package sk.tuke.gamestudio.server.game.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoController {

  @Value("${resource.server.api.uri}")
  String url;

//  @NonNull
  RestTemplate restTemplate;

  @NonNull
  JwtDecoder jwtDecoder;

  @GetMapping("/token")
  public String token(@RequestBody String token) {
    System.out.println(token);
    try {
      var jwt = jwtDecoder.decode(token);
      System.out.println(jwt.getClaims().toString());
      return jwt.getClaims().toString();
    } catch (JwtException e) {
      return e.getMessage();
    }
  }

}