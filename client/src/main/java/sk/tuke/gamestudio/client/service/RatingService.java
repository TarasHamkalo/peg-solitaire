package sk.tuke.gamestudio.client.service;

import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.RatingDto;

public interface RatingService {
  void setRating(RatingDto rating) throws ServiceException;

  int getAverageRating(String game) throws ServiceException;

  int getRating(String game, String player) throws ServiceException;
}
