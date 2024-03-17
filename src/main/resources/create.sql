DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS Score;

CREATE TABLE Comment
(
    player      VARCHAR(64)  NOT NULL,
    game        VARCHAR(64)  NOT NULL,
    text        VARCHAR(150) NOT NULL,
    commentedOn TIMESTAMP    NOT NULL DEFAULT CAST(CURRENT_TIMESTAMP as timestamp),
    PRIMARY KEY (player, game),
    CONSTRAINT not_blank CHECK (
                length(trim(text)) <> 0 AND
                length(trim(player)) <> 0 AND
                length(trim(game)) <> 0
        )
);

CREATE TABLE Rating
(
    player  VARCHAR(64) NOT NULL,
    game    VARCHAR(64) NOT NULL,
    stars   INT         NOT NULL CHECK (stars BETWEEN 1 AND 5),
    ratedOn TIMESTAMP   NOT NULL DEFAULT CAST(CURRENT_TIMESTAMP as timestamp),
    PRIMARY KEY (player, game),
    CONSTRAINT not_blank CHECK (
                length(trim(player)) <> 0 AND
                length(trim(game)) <> 0
        )
);

CREATE TABLE Score
(
    player   VARCHAR(64) NOT NULL,
    game     VARCHAR(64) NOT NULL,
    points   INT         NOT NULL CHECK (points > 0),
    playedOn TIMESTAMP   NOT NULL DEFAULT CAST(CURRENT_TIMESTAMP as timestamp),

    PRIMARY KEY (game, player),
    CONSTRAINT not_blank CHECK (
                length(trim(player)) <> 0 AND
                length(trim(game)) <> 0
        )
);