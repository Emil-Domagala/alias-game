package game.alias.player;

import game.alias.auth.AuthUser;
import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.user.UserRepository;
import game.alias.user.domains.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final UserRepository userRepository;
    private final PlayerCacheRepository playerCacheRepository;
    private final PlayerMapper playerMapper;


    @Override
    public Set<Player> loadExistingPlayers(Set<UUID> userIds) {
        Map<UUID, Player> result = new HashMap<>();

        playerCacheRepository.findAllById(userIds)
                .forEach(player -> result.put(player.getId(), player));

        Set<UUID> missingIds = userIds.stream()
                .filter(id -> !result.containsKey(id))
                .collect(Collectors.toSet());

        userRepository.findAllById(missingIds)
                .forEach(user -> result.put(
                        user.getId(),
                        Player.builder()
                                .id(user.getId())
                                .nick(user.getNick())
                                .build()
                ));

        result.values().forEach(p -> p.setTtl(Duration.ofHours(1).getSeconds()));

        playerCacheRepository.saveAll(result.values());

        return new HashSet<>(result.values());
    }

    @Override
    public Player loadExistingPlayer(UUID userId) {
        Objects.requireNonNull(userId, "userId cannot be null");

        return playerCacheRepository.findById(userId)
                .map(player -> {
                    player.setTtl(Duration.ofHours(1).getSeconds());
                    return playerCacheRepository.save(player);
                })
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new EntityNotFoundException("User with ID " + userId + " not found")
                            );

                    Player player = Player.builder()
                            .id(user.getId())
                            .nick(user.getNick())
                            .ttl(Duration.ofHours(1).getSeconds())
                            .build();

                    return playerCacheRepository.save(player);
                });
    }

    @Override
    public Player cashePlayer(AuthUser user) {
        var foundPlayer = playerCacheRepository.findById(user.getId());
        Player player;

        if (foundPlayer.isPresent()) {
            player = foundPlayer.get();
            player.setTtl(Duration.ofHours(1).getSeconds());
        } else {
            player = Player.builder()
                    .id(user.getId())
                    .nick(user.getNick())
                    .ttl(Duration.ofHours(1).getSeconds())
                    .build();
        }
       return playerCacheRepository.save(player);
    }
}
