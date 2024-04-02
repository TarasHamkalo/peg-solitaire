package sk.tuke.gamestudio.commons.service.impl.rest.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        restTemplate.postForEntity(url + "/scores", score, Score.class);
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        var scores = restTemplate.getForEntity(url + "/scores/" + game, Score[].class).getBody();
        if (scores == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(scores);
    }

    @Override
    public void reset() throws ScoreException {
        throw new ScoreException("Unauthorized to remove scores remotely");
    }
}
