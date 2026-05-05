package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.pagination.PaginationResult;
import game.alias.common.pagination.QueryConfigModel;
import game.alias.common.pagination.QueryFilter;
import game.alias.room.domains.Room;
import game.alias.room.domains.request.CreateRoomRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomService {

    Room create(CreateRoomRequest request, AuthUser user);

    Room delete(UUID roomId, AuthUser user);

    Room leaveRoom(UUID roomId, AuthUser user);

    Room joinRoom(UUID roomId, AuthUser user);

    Room loadRoomOrThrow(UUID roomId);

    RoomWithPlayers findUsersInRoom(UUID roomId);

    RoomWithUsers getRoomWithUsers(UUID roomId);

    PaginationResult<Room> getRooms(Pageable pageable, List<QueryFilter> filters, String search, QueryConfigModel.QueryConfig config);

    Optional<Room> findRoomByPlayer(UUID id);
}
