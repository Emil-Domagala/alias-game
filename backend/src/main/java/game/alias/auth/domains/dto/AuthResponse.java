package game.alias.auth.domains.dto;

import game.alias.user.domains.dto.UserDto;

public record AuthResponse(String sessionId, UserDto UserDto) {

}
