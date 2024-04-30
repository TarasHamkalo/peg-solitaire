package sk.tuke.gamestudio.client.service;

import sk.tuke.gamestudio.client.service.exception.ServiceException;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

import java.util.List;

public interface ScoreService {
  void addScore(ScoreDto score) throws ServiceException;

  List<ScoreDto> getTopScores(String game) throws ServiceException;
}
