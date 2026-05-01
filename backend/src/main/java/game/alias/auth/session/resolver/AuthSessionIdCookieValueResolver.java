package game.alias.auth.session.resolver;

import game.alias.config.properties.SessionConfigProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class AuthSessionIdCookieValueResolver implements HandlerMethodArgumentResolver {
    private final SessionConfigProperties sessionConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthSessionIdCookieValue.class) && parameter.getParameterType().equals(String.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new IllegalStateException("No HttpServletRequest");
        }

        AuthSessionIdCookieValue annotation = parameter.getParameterAnnotation(AuthSessionIdCookieValue.class);

        boolean required = annotation != null && annotation.required();
        String cookieName = sessionConfig.cookie().auth().name();

        Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[0];

        return Arrays.stream(cookies)
                .filter(c -> cookieName.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseGet(() -> {
                    if (required) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing auth cookie: " + cookieName);
                    }
                    return null;
                });
    }
}
