package sk.tuke.gamestudio.server.api.rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreDto implements Serializable {

  Long id;

  @NonNull
  String game;

  @NonNull
  String player;

  @NonNull
  Integer points;

  @Builder.Default
  Date playedOn = Date.from(Instant.now());

  @Override
  public String toString() {
    return String.format("%s scored {%d} on %s.", player, points, playedOn);
  }
}