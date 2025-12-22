package game.alias.user.domains;
import game.alias.user.domains.dto.PlayerDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public PlayerDto toPlayerDto(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDto(player.getId(), player.getUsername());
    }

}
