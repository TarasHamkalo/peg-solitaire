package sk.tuke.gamestudio.server.controller;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Rating;
import sk.tuke.gamestudio.data.service.RatingService;
import sk.tuke.gamestudio.server.aop.annotations.ClaimMapping;
import sk.tuke.gamestudio.server.aop.annotations.MapClaimsToFields;
import sk.tuke.gamestudio.server.api.rest.dto.RatingDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ratings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingController {

  Mapper mapper;

  RatingService ratingService;

  @CacheEvict(
    cacheNames = "avg_rating",
    allEntries = true
  )
  @CachePut(
    cacheNames = "ratings",
    key = "#rating.player"
  )
  @MapClaimsToFields(
    claimMappings = {
      @ClaimMapping(claim = "username", field = "player"),
      @ClaimMapping(claim = "game", field = "game")
    }
  )
  @PostMapping
  public int setRating(@RequestBody RatingDto rating) {
    ratingService.setRating(mapper.map(rating, Rating.class));
    return rating.getStars();
  }

  @Cacheable(
    cacheNames = "ratings",
    key = "#player"
  )
  @GetMapping("/{game}/{player}")
  public int getRating(@PathVariable String game, @PathVariable String player) {
    return ratingService.getRating(game, player);
  }

  @Cacheable(cacheNames = "avg_rating")
  @GetMapping("/{game}")
  public int getAverageRating(@PathVariable String game) {
    return ratingService.getAverageRating(game);
  }
}
