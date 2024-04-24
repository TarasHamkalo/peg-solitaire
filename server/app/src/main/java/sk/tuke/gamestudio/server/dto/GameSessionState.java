package sk.tuke.gamestudio.server.dto;

public record GameSessionState(
    long score,
    boolean won,
    boolean hasMoves,
    boolean reload) {
}
