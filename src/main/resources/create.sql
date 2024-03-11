DROP TABLE IF EXISTS Comment;
DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS Score;

CREATE TABLE Comment
(
    player      VARCHAR(64)  NOT NULL,
    game        VARCHAR(64)  NOT NULL,
    comment     VARCHAR(150) NOT NULL,
    commentedOn TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP::timestamp,
    PRIMARY KEY (player, game),
    CONSTRAINT not_blank CHECK (
        length(trim(comment)) <> 0 AND
        length(trim(player)) <> 0 AND
        length(trim(game)) <> 0
    )
);

CREATE TABLE Rating
(
    player  VARCHAR(64) NOT NULL,
    game    VARCHAR(64) NOT NULL,
    value   INT         NOT NULL CHECK (value BETWEEN 0 AND 5),
    ratedOn TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP::timestamp,
    PRIMARY KEY (player, game),
    CONSTRAINT not_blank CHECK (
        length(trim(player)) <> 0 AND
        length(trim(game)) <> 0
    )
);

CREATE TABLE Score
(
    id       BIGSERIAL   NOT NULL,
    player   VARCHAR(64) NOT NULL,
    game     VARCHAR(64) NOT NULL,
    points   INT         NOT NULL CHECK (points > 0),
    playedOn TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP::timestamp,

    PRIMARY KEY (id),
    CONSTRAINT not_blank CHECK (
        length(trim(player)) <> 0 AND
        length(trim(game)) <> 0
    )
);