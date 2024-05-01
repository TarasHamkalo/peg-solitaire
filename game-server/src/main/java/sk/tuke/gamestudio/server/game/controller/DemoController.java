package sk.tuke.gamestudio.server.game.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoController {

  @Value("${oauth.client.trusted.id}")
  String clientId;

  @NonNull
  OAuth2AuthorizedClientManager authorizedClientManager;

  @NonNull
  ClientRegistration  clientRegistration;

  @GetMapping("/token")
  public String token(Authentication auth) {
    var authRequest = OAuth2AuthorizeRequest
      .withClientRegistrationId(clientRegistration.getClientId())
      .principal("client")
      .build();

    var authorizedClient = authorizedClientManager.authorize(authRequest);
    return authorizedClient.getAccessToken().getTokenValue();
  }
}
