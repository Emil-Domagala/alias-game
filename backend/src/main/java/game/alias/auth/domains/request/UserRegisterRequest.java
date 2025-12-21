package game.alias.auth.domains.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

        @NotBlank(message = "Username is required") @Size(min = UserConstraints.MIN_USERNAME, max = UserConstraints.MAX_USERNAME, message = "Username must be between {min} and {max} characters") String username,

        @NotBlank(message = "Password is required") @Size(min = UserConstraints.MIN_PASSWORD, max = UserConstraints.MAX_PASSWORD, message = "Password must be at least {min} and {max} characters long") String password) {
}
