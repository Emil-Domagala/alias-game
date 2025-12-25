package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomException;
import game.alias.room.domains.RoomStatus;
import game.alias.room.domains.request.CreateRoomRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final RoomCacheRepository roomCacheRepository;
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
            Optional<Room> existingRoom = roomCacheRepository.findByOwnerId(user.getId());
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
                    .ttl(Duration.ofHours(1))
                    .build();
           var savedRoom =  roomCacheRepository.save(roomToSave);

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
            Room room = roomCacheRepository.findById(roomId).orElseThrow(
                    () -> new EntityNotFoundException("Room with such ID does not exist")
            );

            if (!room.getOwnerId().equals(user.getId())) {
                throw new RoomException("You are not the owner of this room");
            }

          if(room.getStatus()== RoomStatus.IN_GAME){
              throw new RoomException("Room is in game and cannot be deleted");
          }

            roomCacheRepository.delete(room);

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
            if(room.getStatus()==RoomStatus.IN_GAME){
                throw new RoomException("Room is in game and cannot be left");
            }

            if(!room.getPlayersId().contains(user.getId())){
                throw new RoomException("User is not a member of this room");
            }

            if(room.getOwnerId().equals(user.getId())){
                delete(roomId, user);
            }

            room.getPlayersId().remove(user.getId());

            if(room.getPlayersId().size()<room.getMinPlayers()){
                room.setStatus(RoomStatus.WAITING);
            }

            roomCacheRepository.save(room);

            roomEventPublisher.playerLeft(room.getId(), user.getId());
            return room;
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
                throw new RoomException("You are already a member of this room");
            }
            if (room.getStatus() != RoomStatus.WAITING) {
                throw new RoomException("Room is not in waiting state");
            }

            if (room.getPlayersId().size() >= room.getMaxPlayers()) {
                throw new RoomException("Room is full");
            }

            room.getPlayersId().add(user.getId());

            if(room.getPlayersId().size()==room.getMaxPlayers()){
                room.setStatus(RoomStatus.FULL);
            }

            roomCacheRepository.save(room);

            roomEventPublisher.playerJoined(room.getId(), user.getId());
            return room;
        } finally {
            redisTemplate.delete(lockKey);
        }
    }

    public Room loadRoomOrThrow(UUID roomId){
        return roomCacheRepository.findById(roomId)
                .orElseThrow(()->new EntityNotFoundException("Room from which user wanna leave do not exists"));
    }
}
