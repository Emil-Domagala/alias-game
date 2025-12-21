package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomException;
import game.alias.room.domains.request.CreateRoomRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final StringRedisTemplate redisTemplate;
    private final RoomEventPublisher roomEventPublisher;

    @Override
    public Room create(CreateRoomRequest request, AuthUser user) {
        UUID userId = user.getId();
        String lockKey = "room:create:lock:" + userId;
        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if(Boolean.FALSE.equals(lockAcquired)){
            throw new RoomException("Room creation already in progress");
        }
        try {
            Optional<Room> existingRoom = roomRepository.findByOwnerId(user.getId());
            if (existingRoom.isPresent()) {
                throw new RoomException("You already own room with ID: " + existingRoom.get().getId());
            }

            Set<UUID> players = new HashSet<>();
            Room roomToSave = Room.builder()
                    .name(request.name())
                    .ownerId(user.getId())
                    .maxPlayers(request.maxPlayers())
                    .minPlayers(request.minPlayers())
                    .playersId(players)
                    .build();
           var savedRoom =  roomRepository.save(roomToSave);

           roomEventPublisher.roomCreated(savedRoom);

           return  savedRoom;
        }finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public Room delete(UUID roomId, AuthUser user) {
        String lockKey = "room:delete:lock:" + roomId;

        Boolean lockAcquired = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.FALSE.equals(lockAcquired)) {
            throw new RoomException("Room deletion already in progress");
        }

        try {
            Room room = roomRepository.findById(roomId).orElseThrow(
                    () -> new EntityNotFoundException("Room with such ID does not exist")
            );

            if (!room.getOwnerId().equals(user.getId())) {
                throw new RoomException("You are not the owner of this room");
            }

            // TODO: Check if game is running
            // if (room.isGameRunning()) throw ...

            roomRepository.delete(room);

            roomEventPublisher.roomDeleted(room);

            return room;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public Room leaveRoom(UUID roomId, AuthUser user){
        String lockKey = "room:lock:" + roomId;

        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.FALSE.equals(locked)) {
            throw new RoomException("Room is busy");
        }

        try{
            Room room = loadRoomOrThrow(roomId);
//            TODO: if game is on lock room
//            TODO: check if room should be deleted (last player or owner)
            throw new NotImplementedException();
        }finally {
            redisTemplate.delete(lockKey);
        }
    }

    @Override
    public Room joinRoom(UUID roomId, AuthUser user) {

        String lockKey = "room:lock:" + roomId;

        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", Duration.ofSeconds(5));

        if (Boolean.FALSE.equals(locked)) {
            throw new RoomException("Room is busy");
        }

        try {
            Room room = loadRoomOrThrow(roomId);

            if (room.getPlayersId().contains(user.getId())) {
                return room;
            }
//            TODO: If game is on lock room;

            if (room.getPlayersId().size() >= room.getMaxPlayers()) {
                throw new RoomException("Room is full");
            }

            room.getPlayersId().add(user.getId());

            roomRepository.save(room);

            roomEventPublisher.playerJoined(room.getId(), user.getId());
            return room;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    public Room loadRoomOrThrow(UUID roomId){
        return roomRepository.findById(roomId)
                .orElseThrow(()->new EntityNotFoundException("Room from which user wanna leave do not exists"));
    }
}
