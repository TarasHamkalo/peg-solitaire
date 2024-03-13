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
public class Score {
    String game;

    String player;

    int points;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    Timestamp playedOn = Timestamp.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s scored {%d} on %s.", player, points, playedOn);
    }
}