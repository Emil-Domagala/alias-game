package game.alias.room.domains.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRoomRequest(

        @NotNull(message = "Max players is required")
        @Min(value = 2, message = "Min players must be at least 2")
        @Max(value = 10, message = "Max players cannot exceed 10")
        Integer maxPlayers,

        @NotNull(message = "Min players is required")
        @Min(value = 2, message = "Min players must be at least 2")
        @Max(value = 10, message = "Max players cannot exceed 10")
        Integer minPlayers,

        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
        String name,

        @NotNull
        @Min(value = 1, message = "Number of teams must be at least 1")
        @Max(value = 5, message = "Number of tems cannot exceed 5")
        Integer numberOfTeams
) {
}
