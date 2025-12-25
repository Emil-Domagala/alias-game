package game.alias.player.domains;
import game.alias.player.domains.dto.PlayerDto;
import game.alias.user.domains.User;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public PlayerDto toPlayerDto(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerDto(player.getId(), player.getNick());
    }

}
