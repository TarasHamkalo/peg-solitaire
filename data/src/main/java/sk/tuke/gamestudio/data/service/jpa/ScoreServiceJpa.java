package sk.tuke.gamestudio.data.service.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.data.entity.Score;
import sk.tuke.gamestudio.data.exception.ScoreException;
import sk.tuke.gamestudio.data.service.ScoreService;

import java.util.Collections;
import java.util.List;

@Repository
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreServiceJpa implements ScoreService {

  EntityManager entityManager;

  @Autowired
  public ScoreServiceJpa(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void addScore(Score score) throws ScoreException {
    try {
      entityManager.persist(score);
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new ScoreException("Was not able to add score", e);
    }
  }

  @Override
  public List<Score> getTopScores(String game) throws ScoreException {
    try {
      var query = entityManager.createQuery(
        "SELECT s FROM Score s WHERE s.game = :game ORDER BY s.points DESC", Score.class
      );

      var result = query.setParameter("game", game)
        .setMaxResults(10)
        .getResultList();

      return result == null ? Collections.emptyList() : result;
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new ScoreException("Was not able to get scores", e);
    }
  }

  @Override
  public void reset() throws ScoreException {
    try {
      entityManager.createQuery("DELETE FROM Score").executeUpdate();
    } catch (ConstraintViolationException | PersistenceException e) {
      throw new ScoreException("Was not able to delete scores", e);
    }
  }
}
