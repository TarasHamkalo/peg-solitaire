package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.commons.entity.Rating;
import sk.tuke.gamestudio.commons.exception.RatingException;
import sk.tuke.gamestudio.commons.service.RatingService;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingServiceRestClient implements RatingService {

    @Value("${remote.server.api}")
    String url;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        try {
            restTemplate.postForObject(url + "/ratings", rating, Rating.class);
        } catch (RestClientException e) {
            throw new RatingException("Was not able to add rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try {
            Integer avgRating = restTemplate.getForObject(url + "/ratings/" + game, Integer.class);

            if (avgRating == null) {
                return 0;
            }

            return avgRating;

        } catch (RestClientException e) {
            throw new RatingException("Was not able to get avg rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Integer rating = restTemplate.getForObject(url + "/ratings/" + game, Integer.class);
            if (rating == null) {
                return 0;
            }

            return rating;
        } catch (RestClientException e) {
            throw new RatingException("Was not able to get rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        throw new RatingException("Unauthorized to remove ratings remotely");
    }
}
