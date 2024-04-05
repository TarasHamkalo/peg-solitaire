package sk.tuke.gamestudio.data.service;

import sk.tuke.gamestudio.data.entity.Rating;
import sk.tuke.gamestudio.data.exception.RatingException;

public interface RatingService {
    void setRating(Rating rating) throws RatingException;

    int getAverageRating(String game) throws RatingException;

    int getRating(String game, String player) throws RatingException;

    void reset() throws RatingException;
}
