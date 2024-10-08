package sk.tuke.gamestudio.server.controller;

import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.data.entity.Comment;
import sk.tuke.gamestudio.data.service.CommentService;
import sk.tuke.gamestudio.server.aop.annotations.ClaimMapping;
import sk.tuke.gamestudio.server.aop.annotations.MapClaimsToFields;
import sk.tuke.gamestudio.server.api.rest.dto.CommentDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/comments")
@CacheConfig(cacheNames = "comments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

  Mapper mapper;

  CommentService commentService;

  @CacheEvict(allEntries = true)
  @MapClaimsToFields(
    claimMappings = {
      @ClaimMapping(claim = "username", field = "player"),
      @ClaimMapping(claim = "game", field = "game")
    }
  )
  @PostMapping
  public void addComment(@RequestBody CommentDto comment) {
    commentService.addComment(mapper.map(comment, Comment.class));
  }

  @Cacheable
  @GetMapping("/{game}")
  public List<CommentDto> getComments(@PathVariable String game) {
    return commentService.getComments(game).parallelStream()
      .map(c -> mapper.map(c, CommentDto.class))
      .toList();
  }
}
