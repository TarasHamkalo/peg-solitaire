package sk.tuke.gamestudio.server.controller.rest;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Score;
import sk.tuke.gamestudio.data.service.ScoreService;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {

    ScoreService scoreService;

    Mapper mapper;

    @Autowired
    public ScoreController(ScoreService scoreService, Mapper mapper) {
        this.scoreService = scoreService;
        this.mapper = mapper;
    }

    @GetMapping("/{game}")
    public List<ScoreDto> getTopScores(@PathVariable String game) {
        return scoreService.getTopScores(game).parallelStream()
            .map(s -> mapper.map(s, ScoreDto.class))
            .toList();
    }

    @PostMapping
    public void addScore(@RequestBody ScoreDto score) {
        scoreService.addScore(mapper.map(score, Score.class));
    }
}
