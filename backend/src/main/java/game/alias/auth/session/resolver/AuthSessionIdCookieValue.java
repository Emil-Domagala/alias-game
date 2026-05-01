package game.alias.auth.session.resolver;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthSessionIdCookieValue {
    boolean required() default true;
}
