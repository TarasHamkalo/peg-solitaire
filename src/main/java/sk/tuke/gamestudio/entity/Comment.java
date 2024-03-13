package sk.tuke.gamestudio.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    String player;

    String game;

    String text;

    @Builder.Default
    @EqualsAndHashCode.Exclude
    Timestamp commentedOn = Timestamp.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s: %s", player, text);
    }
}