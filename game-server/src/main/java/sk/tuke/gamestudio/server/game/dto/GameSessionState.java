package sk.tuke.gamestudio.server.game.dto;

public record GameSessionState(
  long score,
  boolean won,
  boolean hasMoves,
  boolean reload) {
}
