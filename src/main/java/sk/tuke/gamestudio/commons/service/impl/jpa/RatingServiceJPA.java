package sk.tuke.gamestudio.commons.service.impl.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sk.tuke.gamestudio.commons.entity.Rating;
import sk.tuke.gamestudio.commons.exception.RatingException;
import sk.tuke.gamestudio.commons.service.RatingService;


@Repository
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceJPA implements RatingService {

    EntityManager entityManager;

    @Autowired
    public RatingServiceJPA(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void setRating(Rating rating) throws RatingException {
        var builder = entityManager.getCriteriaBuilder();
        var criteria = builder.createQuery(Rating.class);
        var from = criteria.from(Rating.class);

        var findQuery = entityManager.createQuery(criteria.select(from).where(
            builder.equal(from.get("player"), rating.getPlayer()),
            builder.equal(from.get("game"), rating.getGame())
        ));

        try {
            updateOrPersistRating(rating, findQuery);
        } catch (PersistenceException e) {
            throw new RatingException("Was not able to set rating", e);
        }
    }

    private void updateOrPersistRating(Rating rating, TypedQuery<Rating> findQuery) {
        try {
            var retrieved = findQuery.getSingleResult();
            retrieved.setStars(rating.getStars());
            retrieved.setRatedOn(rating.getRatedOn());

        } catch (NoResultException ratingDoesNoExist) {
            entityManager.persist(rating);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        var query = entityManager.createQuery(
            "SELECT AVG(stars) FROM Rating r WHERE r.game = :game", Double.class
        );

        query.setParameter("game", game);

        try {
            Double result = query.getSingleResult();

            return result == null ? 0 : result.intValue();
        } catch (PersistenceException e) {
            throw new RatingException("Was not able to get avg rating", e);
        }

    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        var builder = entityManager.getCriteriaBuilder();
        var criteria = builder.createQuery(Integer.class);
        var from = criteria.from(Rating.class);

        var selectStarsQuery = entityManager.createQuery(criteria.select(from.get("stars")).where(
            builder.equal(from.get("player"), player),
            builder.equal(from.get("game"), game)
        ));

        try {
            return selectStarsQuery.getSingleResult();
        } catch (PersistenceException e) {
            throw new RatingException("Was not able to retrieve rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try {
            entityManager.createQuery("DELETE FROM Rating r").executeUpdate();
        } catch (PersistenceException e) {
            throw new RatingException("Was not able to delete ratings", e);
        }
    }
}
