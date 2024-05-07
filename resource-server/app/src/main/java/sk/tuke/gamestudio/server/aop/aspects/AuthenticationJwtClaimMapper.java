package sk.tuke.gamestudio.server.aop.aspects;

import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import sk.tuke.gamestudio.server.aop.annotations.ClaimMapping;
import sk.tuke.gamestudio.server.aop.annotations.MapClaimsToFields;
import sk.tuke.gamestudio.server.aop.exception.AuthenticationJwtClaimsMapperException;

@Aspect
@AllArgsConstructor
public class AuthenticationJwtClaimMapper {

  @Around("@annotation(sk.tuke.gamestudio.server.aop.annotations.MapClaimsToFields)")
  public Object mapJwtClaimsToFields(ProceedingJoinPoint joinPoint) throws Throwable {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof JwtAuthenticationToken token) {
      var mappedTargetArgument = mapClaimsOntoTargetObject(token, joinPoint);
      return joinPoint.proceed(new Object[]{mappedTargetArgument});
    }

    throw new AuthenticationJwtClaimsMapperException("Unsupported authentication type");
  }

  private Object mapClaimsOntoTargetObject(JwtAuthenticationToken token, JoinPoint joinPoint) {
    var methodSignature = (MethodSignature) joinPoint.getSignature();
    var mapClaimsToFields = methodSignature.getMethod()
      .getDeclaredAnnotation(MapClaimsToFields.class);

    // TODO: mapping of only argument supported
    final var target = joinPoint.getArgs()[0];

    try {
      for (ClaimMapping mapping : mapClaimsToFields.claimMappings()) {
        var targetField = target.getClass().getDeclaredField(mapping.field());
        targetField.setAccessible(true);
        targetField.set(target, token.getToken().getClaim(mapping.claim()));
      }
    } catch (ReflectiveOperationException e) {
      throw new AuthenticationJwtClaimsMapperException(e.getMessage());
    }

    return target;
  }
}
