package sk.tuke.gamestudio.server.controller;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Score;
import sk.tuke.gamestudio.data.service.ScoreService;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/scores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {

  Mapper mapper;

  ScoreService scoreService;

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
