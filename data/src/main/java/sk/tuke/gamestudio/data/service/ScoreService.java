package sk.tuke.gamestudio.data.service;

import sk.tuke.gamestudio.data.entity.Score;
import sk.tuke.gamestudio.data.exception.ScoreException;

import java.util.List;

public interface ScoreService {
  void addScore(Score score) throws ScoreException;

  List<Score> getTopScores(String game) throws ScoreException;

  void reset() throws ScoreException;
}
