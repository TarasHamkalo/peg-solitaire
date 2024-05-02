package sk.tuke.gamestudio.server.aop.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ClaimMapping {

  String claim();

  String field();
}
