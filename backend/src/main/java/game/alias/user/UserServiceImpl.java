package game.alias.user;

import game.alias.user.domains.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public User loadOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
    }

    @Override
    public List<User> findAllByIds(List<UUID> userIds) {
        return userRepository.findAllById(userIds);
    }

}
