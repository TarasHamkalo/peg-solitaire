package sk.tuke.gamestudio.commons.service;

import sk.tuke.gamestudio.commons.entity.Rating;
import sk.tuke.gamestudio.commons.exception.RatingException;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;
}
