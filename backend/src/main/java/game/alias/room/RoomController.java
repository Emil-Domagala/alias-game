package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.ApiVersion;
import game.alias.common.currentUser.CurrentUser;
import game.alias.common.pagination.*;
import game.alias.player.domains.Player;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomMapper;
import game.alias.room.domains.dto.RoomSummaryDto;
import game.alias.room.domains.request.CreateRoomRequest;
import game.alias.player.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApiVersion.V1Private + "/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;
    private final RoomMapper mapper;

    private final PlayerService playerService;
    private final RoomQueryConfigProvider queryConfigProvider;

    @GetMapping("/config")
    public ResponseEntity<QueryConfigModel.QueryConfig> getRoomsConfig() {
        return ResponseEntity.ok(queryConfigProvider.getConfig());
    }

    @PostMapping("/create")
    public ResponseEntity<RoomSummaryDto>createRoom(@Valid @RequestBody CreateRoomRequest request, @CurrentUser AuthUser user){
        var room = service.create(request, user);
        var ownerPlayer = playerService.cashePlayer(user);
        var roomDto = mapper.toRoomSummaryDto(room, ownerPlayer);
        URI location = URI.create("/room/" + room.getId());
        return ResponseEntity.created(location).body(roomDto);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void>deleteRoom(@PathVariable UUID roomId, @CurrentUser AuthUser user){
        service.delete(roomId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<PaginationResult<RoomSummaryDto>>getRooms(
            Pageable pageable,
            @RequestParam(required = false) List<String> filters,
            @RequestParam(required = false) String search
    ){
        QueryConfigModel.QueryConfig config = queryConfigProvider.getConfig();
        List<QueryFilter> filterList = filters != null ? FilterParser.parse(filters) : List.of();

        Pageable validatedPageable = QueryValidator.validatePageable(pageable, config);
        QueryValidator.validateFilters(filterList, config);

        PaginationResult<Room> paginatedRooms = service.getRooms(validatedPageable, filterList,  search, config);
        Set<UUID> ownersId = paginatedRooms.getContent().stream().map(Room::getOwnerId).collect(Collectors.toSet());
        Map<UUID, Player> ownersById = playerService.loadExistingPlayers(ownersId).stream().collect(Collectors.toMap(Player::getId, Function.identity()));
        List<RoomSummaryDto> roomDtos = paginatedRooms.getContent().stream().map(room->mapper.toRoomSummaryDto(room,ownersById.get(room.getOwnerId()))).toList();

        PaginationResult<RoomSummaryDto> result =
                new PaginationResult<>(
                        roomDtos,
                        paginatedRooms.getTotalPages(),
                        paginatedRooms.getTotalElements(),
                        paginatedRooms.getSize(),
                        paginatedRooms.getPage(),
                        paginatedRooms.isEmpty()
                );

        return ResponseEntity.ok(result);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<RoomSummaryDto>joinRoom(@PathVariable UUID roomId, @CurrentUser AuthUser user){
        Room room = service.joinRoom(roomId, user);
        Player currentPlayer = playerService.cashePlayer(user);
        RoomSummaryDto roomDto = mapper.toRoomSummaryDto(room, currentPlayer);

        return ResponseEntity.ok(roomDto);
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<RoomSummaryDto>leaveRoom(@PathVariable UUID roomId, @CurrentUser AuthUser user){
        Room room = service.leaveRoom(roomId, user);

        // If user was owner and deleted room, room will no longer exist
        if (room.getPlayersId().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Player currentPlayer = playerService.cashePlayer(user);
        RoomSummaryDto roomDto = mapper.toRoomSummaryDto(room, currentPlayer);

        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/current")
    public ResponseEntity<RoomSummaryDto> getCurrentRoom(@CurrentUser AuthUser user) {
        Optional<Room> roomOpt = service.findRoomByPlayer(user.getId());

        if (roomOpt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Room room = roomOpt.get();
        var player = playerService.cashePlayer(user);
        RoomSummaryDto roomDto = mapper.toRoomSummaryDto(room, player);

        return ResponseEntity.ok(roomDto);
    }
}
