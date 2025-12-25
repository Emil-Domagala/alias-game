package game.alias.auth.domains.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Nick is required") @Size(min = UserConstraints.MIN_NICK, max = UserConstraints.MAX_NICK, message = "Nick must be between {min} and {max} characters") String nick,

        @NotBlank(message = "Password is required") @Size(min = UserConstraints.MIN_PASSWORD, max = UserConstraints.MAX_PASSWORD, message = "Password must be at least {min} and {max} characters long") String password) {
}
