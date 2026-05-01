package game.alias.config;

import game.alias.auth.session.resolver.AuthSessionIdCookieValueResolver;
import game.alias.common.currentUser.CurrentUserHttpArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final CurrentUserHttpArgumentResolver currentUserHttpResolver;
    private final AuthSessionIdCookieValueResolver authSessionIdResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentUserHttpResolver);
        resolvers.add(authSessionIdResolver);
    }
}