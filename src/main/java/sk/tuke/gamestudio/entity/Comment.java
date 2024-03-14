package sk.tuke.gamestudio.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    String player;

    String game;

    String text;

    @Builder.Default
    Date commentedOn = Date.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s: %s", player, text);
    }
}