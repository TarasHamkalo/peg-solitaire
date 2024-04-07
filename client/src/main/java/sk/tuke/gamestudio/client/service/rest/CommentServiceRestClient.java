package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.client.service.CommentService;
import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.CommentDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceRestClient implements CommentService {

    @Value("${remote.server.api}")
    String url;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void addComment(CommentDto comment) throws ServiceException {
        try {
            restTemplate.postForObject(url + "/comments", comment, CommentDto.class);
        } catch (RestClientException e) {
            throw new ServiceException("Was not able to add comment", e);
        }
    }

    @Override
    public List<CommentDto> getComments(String game) throws ServiceException {
        try {
            var comments = restTemplate.getForObject(url + "/comments/" + game, CommentDto[].class);
            if (comments == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(comments);
        } catch (RestClientException e) {
            throw new ServiceException("Was not able to get comments", e);
        }
    }

}
