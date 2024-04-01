package sk.tuke.gamestudio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rating implements Serializable {
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

    @Min(1)
    @Max(5)
    @NonNull
    @Column(
        nullable = false
    )
    Integer stars;

    @Builder.Default
    @Column(nullable = false)
    Date ratedOn = Date.from(Instant.now());

    @Override
    public String toString() {
        return String.format("%s rated %s with {%d}", player, game, stars);
    }
}