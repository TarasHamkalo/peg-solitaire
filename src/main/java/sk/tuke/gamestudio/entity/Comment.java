package sk.tuke.gamestudio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    @NotBlank
    @Column(
        nullable = false,
        length = 64
    )
    String player;

    @NonNull
    @NotBlank
    @Column(
        nullable = false,
        length = 64
    )
    String game;

    @NonNull
    @NotBlank
    @Column(
        nullable = false,
        length = 150
    )
    String text;

    @Builder.Default
    @Column(nullable = false)
    Date commentedOn = Date.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s: %s", player, text);
    }
}