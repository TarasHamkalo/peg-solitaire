package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.commons.entity.Score;
import sk.tuke.gamestudio.commons.exception.ScoreException;
import sk.tuke.gamestudio.commons.service.ScoreService;

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
    public void addScore(Score score) throws ScoreException {
        try {
            restTemplate.postForEntity(url + "/scores", score, Score.class);
        } catch (RestClientException e) {
            throw new ScoreException("Was not able to add score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try {
            var scores =
                restTemplate.getForEntity(url + "/scores/" + game, Score[].class).getBody();

            if (scores == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(scores);

        } catch (RestClientException e) {
            throw new ScoreException("Was not able to add score", e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        throw new ScoreException("Unauthorized to remove scores remotely");
    }
}
