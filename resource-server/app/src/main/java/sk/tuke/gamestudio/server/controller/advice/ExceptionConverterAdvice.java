package sk.tuke.gamestudio.server.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sk.tuke.gamestudio.data.exception.CommentException;
import sk.tuke.gamestudio.data.exception.RatingException;
import sk.tuke.gamestudio.data.exception.ScoreException;

import java.net.URI;

@RestControllerAdvice
public class ExceptionConverterAdvice {

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

}
