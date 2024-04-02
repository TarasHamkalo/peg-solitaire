package sk.tuke.gamestudio.commons.service;

import sk.tuke.gamestudio.commons.entity.Score;
import sk.tuke.gamestudio.commons.exception.ScoreException;

import java.util.List;

public interface ScoreService {
    void addScore(Score score) throws ScoreException;

    List<Score> getTopScores(String game) throws ScoreException;

    void reset() throws ScoreException;
}
