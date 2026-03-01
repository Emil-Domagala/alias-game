package game.alias.team.domain.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTeamRequest(
        @NotNull(message = "Name is required")
        @Size(min = 3, max = 255, message = "Name must be between {min} and {max} characters")
        String name) {

}
