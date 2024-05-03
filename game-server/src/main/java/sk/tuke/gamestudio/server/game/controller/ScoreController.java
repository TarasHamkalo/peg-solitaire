package sk.tuke.gamestudio.server.game.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.pegsolitaire.core.game.Game;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

@RestController
@RequestMapping("/pegsolitaire")
@RequiredArgsConstructor
public class ScoreController {

  @Value("${resource.server.api.uri}")
  String url;

  @NonNull
  RestTemplate restTemplate;

  @NonNull
  JwtDecoder jwtDecoder;

  @NonNull
  Game game;

  @PostMapping("/score")
  public void addScore(@RequestBody String rawToken) {
    if (game.getBoard() == null) {
      return;
    }

    if (game.getScore() == 0) {
      return;
    }

    var token = jwtDecoder.decode(rawToken);
    System.out.println(token.getClaims());

    var score = ScoreDto.builder()
      .game(token.getClaimAsString("game"))
      .player(token.getClaimAsString("username"))
      .points((int) game.getScore())
      .build();

    restTemplate.postForEntity(url + "/scores", score, ScoreDto.class);
    game.stop();
  }

}
