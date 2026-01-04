package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.pagination.PaginationRequest;
import game.alias.common.pagination.PaginationResult;
import game.alias.room.domains.Room;
import game.alias.room.domains.request.CreateRoomRequest;

import java.util.UUID;

public interface RoomService {

    Room create(CreateRoomRequest request, AuthUser user);

    Room delete(UUID roomId, AuthUser user);

    Room leaveRoom(UUID roomId, AuthUser user);

    Room joinRoom(UUID roomId, AuthUser user);

    Room loadRoomOrThrow(UUID roomId);

    PaginationResult<Room> getRooms(PaginationRequest request);
}
