package sk.tuke.gamestudio.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    String player;

    String game;

    int stars;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    Timestamp ratedOn = Timestamp.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s rated %s with {%d}", player, game, stars);
    }
}