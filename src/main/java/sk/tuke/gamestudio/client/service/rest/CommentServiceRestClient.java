package sk.tuke.gamestudio.client.service.rest;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.commons.entity.Comment;
import sk.tuke.gamestudio.commons.exception.CommentException;
import sk.tuke.gamestudio.commons.service.CommentService;

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
    public void addComment(Comment comment) throws CommentException {
        try {
            restTemplate.postForObject(url + "/comments", comment, Comment.class);

        } catch (RestClientException e) {
            throw new CommentException("Was not able to add comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try {
            var comments = restTemplate.getForObject(url + "/comments/" + game, Comment[].class);
            if (comments == null) {
                return Collections.emptyList();
            }

            return Arrays.asList(comments);
        } catch (RestClientException e) {
            throw new CommentException("Was not able to get comments", e);
        }
    }

    @Override
    public void reset() throws CommentException {
        throw new CommentException("Unauthorized to remove comments remotely");
    }
}
