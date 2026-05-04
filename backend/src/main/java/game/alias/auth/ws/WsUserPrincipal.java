package game.alias.auth.ws;

import game.alias.auth.AuthUser;

import java.security.Principal;

public record WsUserPrincipal(AuthUser user) implements Principal {
    @Override
    public String getName() {
        return user.getId().toString();
    }

}
