package game.alias.user;

import game.alias.auth.AuthUser;
import game.alias.common.ApiVersion;
import game.alias.user.domains.User;
import game.alias.user.domains.UserMapper;
import game.alias.user.domains.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.V1Private + "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(@AuthenticationPrincipal AuthUser aUser) {
        User user = userService.loadOrThrow(aUser.getId());
        UserDto userDto = UserMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }
}
