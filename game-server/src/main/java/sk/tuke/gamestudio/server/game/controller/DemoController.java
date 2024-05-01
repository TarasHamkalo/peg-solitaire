package sk.tuke.gamestudio.server.game.controller;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.server.api.rest.dto.CommentDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DemoController {

  @Value("${resource.server.api.uri}")
  String url;

  @NonNull
  RestTemplate restTemplate;

  @GetMapping("/comments/{game}")
  public List<CommentDto> getComments(@PathVariable String game) {
    try {
      var comments = restTemplate.getForObject(url + "/comments/" + game, CommentDto[].class);
      if (comments == null) {
        return Collections.emptyList();
      }

      return Arrays.asList(comments);
    } catch (RestClientException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}
