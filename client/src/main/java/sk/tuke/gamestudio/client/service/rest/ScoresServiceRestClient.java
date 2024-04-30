package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.service.ScoreService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoresServiceRestClient implements ScoreService {
  @Value("${remote.server.api}")
  String url;

  @Autowired
  RestTemplate restTemplate;

  @Override
  public void addScore(ScoreDto score) throws ServiceException {
    try {
      restTemplate.postForEntity(url + "/scores", score, ScoreDto.class);
    } catch (RestClientException e) {
      throw new ServiceException("Was not able to add score", e);
    }
  }

  @Override
  public List<ScoreDto> getTopScores(String game) throws ServiceException {
    try {
      var scores = restTemplate.getForEntity(url + "/scores/" + game, ScoreDto[].class).getBody();

      if (scores == null) {
        return Collections.emptyList();
      }

      return Arrays.asList(scores);

    } catch (RestClientException e) {
      throw new ServiceException("Was not able to add score", e);
    }
  }
}
