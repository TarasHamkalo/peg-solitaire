package sk.tuke.gamestudio.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

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
        length = 64
    )
    String player;

    @NonNull
    @Positive
    Integer points;

    @Builder.Default
    @Column(nullable = false)
    Date playedOn = Date.from(Instant.now());
}