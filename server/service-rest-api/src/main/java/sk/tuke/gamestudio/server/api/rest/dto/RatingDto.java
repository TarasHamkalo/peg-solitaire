package sk.tuke.gamestudio.server.api.rest.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingDto {

    Long id;

    @NonNull
    String player;

    @NonNull
    String game;

    @NonNull
    Integer stars;

    @Builder.Default
    Date ratedOn = Date.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s rated %s with {%d}", player, game, stars);
    }
}
