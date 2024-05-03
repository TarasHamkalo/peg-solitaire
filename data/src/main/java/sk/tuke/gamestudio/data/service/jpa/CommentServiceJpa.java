package sk.tuke.gamestudio.data.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.data.entity.Comment;
import sk.tuke.gamestudio.data.exception.CommentException;
import sk.tuke.gamestudio.data.service.CommentService;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceJpa implements CommentService {

  EntityManager entityManager;

  @Autowired
  public CommentServiceJpa(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void addComment(Comment comment) throws CommentException {
    try {
      entityManager.persist(comment);
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new CommentException("Was not able to add comment", e);
    }
  }

  @Override
  public List<Comment> getComments(String game) throws CommentException {
    try {
      var result = entityManager
        .createQuery(
          "SELECT c FROM Comment c WHERE c.game = :game ORDER BY commentedOn DESC", Comment.class)
        .setParameter("game", game)
        .setMaxResults(10)
        .getResultList();

      return result == null ? Collections.emptyList() : result;
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new CommentException("Was not able to get comments", e);
    }
  }

  @Override
  public void reset() throws CommentException {
    try {
      entityManager.createQuery("DELETE FROM Comment").executeUpdate();
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new CommentException("Was not able to delete comments", e);
    }
  }
}
