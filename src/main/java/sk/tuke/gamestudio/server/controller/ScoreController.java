package sk.tuke.gamestudio.server.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.commons.entity.Score;
import sk.tuke.gamestudio.commons.service.ScoreService;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {

    ScoreService scoreService;

    @Autowired
    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/{game}")
    public List<Score> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game);
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }
}
