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
public class CommentDto implements Serializable {

  Long id;

  String player;

  String game;

  @NonNull
  String text;

  @Builder.Default
  Date commentedOn = Date.from(Instant.now());

  @Override
  public String toString() {
    return String.format("%s: %s", player, text);
  }
}