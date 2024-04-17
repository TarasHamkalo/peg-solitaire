package sk.tuke.gamestudio.server.handlers;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.tuke.gamestudio.data.exception.CommentException;
import sk.tuke.gamestudio.data.exception.RatingException;
import sk.tuke.gamestudio.data.exception.ScoreException;
import sk.tuke.gamestudio.pegsolitaire.core.levels.LevelBuilder;
import sk.tuke.gamestudio.server.dto.SetupForm;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameStudioAdvice {

    @Autowired
    List<Class<? extends LevelBuilder>> levelBuilders;

    @Autowired
    SetupForm setupForm;

    private static ResponseEntity<ProblemDetail> createResponse(String message, String errorType) {
        var problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, message
        );

        problemDetail.setType(URI.create(errorType));
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler({
        ScoreException.class,
        RatingException.class,
        CommentException.class
    })
    public ResponseEntity<ProblemDetail> handleProductNotFoundException(Exception e) {
        return createResponse(e.getMessage(), e.getClass().getSimpleName());
    }

    @ModelAttribute("levels")
    public List<Class<? extends LevelBuilder>> levels() {
        return levelBuilders;
    }

    @ModelAttribute("setupForm")
    public SetupForm setupForm() {
        return setupForm;
    }
}
