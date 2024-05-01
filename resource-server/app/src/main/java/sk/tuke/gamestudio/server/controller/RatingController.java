package sk.tuke.gamestudio.server.controller;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Rating;
import sk.tuke.gamestudio.data.service.RatingService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ratings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

  Mapper mapper;

  RatingService ratingService;

  @PostMapping
  public void setRating(@RequestBody Rating rating) {
    ratingService.setRating(mapper.map(rating, Rating.class));
  }

  @GetMapping("/{game}/{player}")
  public int getRating(@PathVariable String game, @PathVariable String player) {
    return ratingService.getRating(game, player);
  }

  @GetMapping("/{game}")
  public int getAverageRating(@PathVariable String game) {
    return ratingService.getAverageRating(game);
  }
}
