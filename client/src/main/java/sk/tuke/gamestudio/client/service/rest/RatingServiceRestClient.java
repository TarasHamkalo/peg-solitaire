package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.service.RatingService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.RatingDto;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingServiceRestClient implements RatingService {

    @Value("${remote.server.api}")
    String url;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void setRating(RatingDto rating) throws ServiceException {
        try {
            restTemplate.postForObject(url + "/ratings", rating, RatingDto.class);
        } catch (RestClientException e) {
            throw new ServiceException("Was not able to add rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws ServiceException {
        try {
            Integer avgRating = restTemplate.getForObject(
                url + "/ratings/" + game, Integer.class
            );

            if (avgRating == null) {
                return 0;
            }

            return avgRating;

        } catch (RestClientException e) {
            throw new ServiceException("Was not able to get avg rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws ServiceException {
        try {
            Integer rating = restTemplate.getForObject(
                url + "/ratings/" + game + "/" + player, Integer.class
            );

            if (rating == null) {
                return 0;
            }

            return rating;
        } catch (RestClientException e) {
            throw new ServiceException("Was not able to get rating", e);
        }
    }

}
