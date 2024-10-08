package sk.tuke.gamestudio.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment implements Serializable {

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

}