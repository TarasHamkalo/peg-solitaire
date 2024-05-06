package sk.tuke.gamestudio.server.controller;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Score;
import sk.tuke.gamestudio.data.service.ScoreService;
import sk.tuke.gamestudio.server.api.rest.dto.ScoreDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/scores")
@CacheConfig(cacheNames = "scores")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ScoreController {

  Mapper mapper;

  ScoreService scoreService;

  @Cacheable
  @GetMapping("/{game}")
  public List<ScoreDto> getTopScores(@PathVariable String game) {
    return scoreService.getTopScores(game).parallelStream()
      .map(s -> mapper.map(s, ScoreDto.class))
      .toList();
  }

  @CacheEvict(allEntries = true)
  @PostMapping
  public void addScore(@RequestBody ScoreDto score) {
    scoreService.addScore(mapper.map(score, Score.class));
  }
}
