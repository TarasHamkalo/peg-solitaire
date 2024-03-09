package sk.tuke.gamestudio.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    String player;

    String game;

    String comment;

    Timestamp commentedOn;
}
