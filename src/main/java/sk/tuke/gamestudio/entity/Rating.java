package sk.tuke.gamestudio.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating {
    String player;

    String game;

    int value;

    @Builder.Default
    Timestamp ratedOn = Timestamp.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s rated %s with {%d}", player, game, value);
    }
}