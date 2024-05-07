package sk.tuke.gamestudio.server.aop.exception;

public class AuthenticationJwtClaimsMapperException extends RuntimeException {

  public AuthenticationJwtClaimsMapperException(String message) {
    super(message);
  }

  public AuthenticationJwtClaimsMapperException(String message, Throwable cause) {
    super(message, cause);
  }

}
