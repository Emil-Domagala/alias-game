package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.pagination.PaginationResult;
import game.alias.common.pagination.QueryFilter;
import game.alias.player.PlayerService;
import game.alias.player.domains.Player;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomException;
import game.alias.room.domains.RoomStatus;
import game.alias.room.domains.dto.RoomWithPlayers;
import game.alias.room.domains.dto.RoomWithUsers;
import game.alias.room.domains.request.CreateRoomRequest;
import game.alias.user.UserService;
import game.alias.user.domains.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final UserService userService;
    private final RoomRepository roomRepository;
    private final PlayerService playerService;
    private final StringRedisTemplate redisTemplate;
    private final RoomEventPublisher roomEventPublisher;
    private final RoomSpecificationBuilder roomSpecificationBuilder;

    /* --------------------------- ROOM FETCH --------------------------- */

    @Override
    public RoomWithPlayers findUsersInRoom(UUID roomId) {
        Room room = loadRoomOrThrow(roomId);
        List<Player> players = playerService.findAllByIds(new ArrayList<>(room.getPlayersId()));
        return new RoomWithPlayers(room, players);
    }

    @Override
    public RoomWithUsers getRoomWithUsers(UUID roomId) {
        Room room = loadRoomOrThrow(roomId);
        List<User> users = userService.findAllByIds(new ArrayList<>(room.getPlayersId()));
        return new RoomWithUsers(room, users);
    }

    @Override
    public PaginationResult<Room> getRooms(Pageable pageable, List<QueryFilter> filters) {
        Specification<Room> spec = filters.stream()
                        .map(roomSpecificationBuilder::buildFilterSpecification)
                        .reduce(Specification::and)
                        .orElse(null);

        Page<Room> page = roomRepository.findAll(spec, pageable);

        return new PaginationResult<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.isEmpty()
        );
    }

    @Override
    public Optional<Room> findRoomByPlayer(UUID playerId) {
        return roomRepository.findByPlayerId(playerId);
    }

    /* --------------------------- ROOM CREATION --------------------------- */

    @Override
    public Room create(CreateRoomRequest request, AuthUser user) {
        return withRedisLock("room:create:lock:" + user.getId(), () -> {
            roomRepository.findByOwnerId(user.getId())
                    .ifPresent(r -> { throw new RoomException("You already own room with ID: " + r.getId()); });

            int playersPerTeam = request.maxPlayers() / request.numberOfTeams();
            if (playersPerTeam < 2) {
                throw new RoomException("Team must have at least 2 people. Adjust min players or number of teams.");
            }


            Room room = Room.builder()
                    .name(request.name())
                    .ownerId(user.getId())
                    .maxPlayers(request.maxPlayers())
                    .minPlayers(request.minPlayers())
                    .numberOfTeams(request.numberOfTeams())
                    .status(RoomStatus.WAITING)
                    .playersId(List.of(user.getId()))
                    .build();

            Room savedRoom = roomRepository.save(room);
            roomEventPublisher.roomCreated(savedRoom);
            return savedRoom;
        });
    }

    /* --------------------------- ROOM JOIN/LEAVE --------------------------- */

    @Override
    public Room joinRoom(UUID roomId, AuthUser user) {
        return withRedisLock("room:lock:" + roomId, () -> {
            Room existingRoom = roomRepository.findByPlayerId(user.getId()).orElse(null);
            if (existingRoom != null) {
                throw new RoomException(
                        "You are already a member of room: " + existingRoom.getId()
                );
            }
            Room room = loadRoomOrThrow(roomId);

            if (room.getStatus() != RoomStatus.WAITING) {
                throw new RoomException("Room is not in waiting state");
            }
            if (room.getPlayersId().size() >= room.getMaxPlayers()) {
                throw new RoomException("Room is full");
            }

            room.getPlayersId().add(user.getId());
            if (room.getPlayersId().size() == room.getMaxPlayers()) {
                room.setStatus(RoomStatus.FULL);
            }

            Room savedRoom = roomRepository.save(room);
            roomEventPublisher.playerJoined(room.getId(), user.getId());
            return savedRoom;
        });
    }

    @Override
    public Room leaveRoom(UUID roomId, AuthUser user) {
        return withRedisLock("room:lock:" + roomId, () -> {
            Room room = loadRoomOrThrow(roomId);

            if (room.getStatus() == RoomStatus.IN_GAME) {
                throw new RoomException("Room is in game and cannot be left");
            }
            if (!room.getPlayersId().contains(user.getId())) {
                throw new RoomException("User is not a member of this room");
            }

            if (room.getOwnerId().equals(user.getId())) {
                delete(roomId, user);
                return room;
            }

            room.getPlayersId().remove(user.getId());
            if (room.getPlayersId().size() < room.getMinPlayers()) {
                room.setStatus(RoomStatus.WAITING);
            }

            Room savedRoom = roomRepository.save(room);
            roomEventPublisher.playerLeft(room.getId(), user.getId());
            return savedRoom;
        });
    }

    /* --------------------------- ROOM DELETE --------------------------- */

    @Override
    public Room delete(UUID roomId, AuthUser user) {
        return withRedisLock("room:delete:lock:" + roomId, () -> {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new EntityNotFoundException("Room with such ID does not exist"));

            if (!room.getOwnerId().equals(user.getId())) {
                throw new RoomException("You are not the owner of this room");
            }
            if (room.getStatus() == RoomStatus.IN_GAME) {
                throw new RoomException("Room is in game and cannot be deleted");
            }

            roomRepository.delete(room);
            roomEventPublisher.roomDeleted(room);
            return room;
        });
    }

    /* --------------------------- HELPERS --------------------------- */

    public Room loadRoomOrThrow(UUID roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room does not exist"));
    }

    /**
     * Executes a supplier with a Redis lock, automatically releasing after execution.
     */
    private <T> T withRedisLock(String lockKey, Supplier<T> action) {
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.FALSE.equals(locked)) {
            throw new RoomException("Operation in progress, please try again later");
        }

        try {
            return action.get();
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

}
