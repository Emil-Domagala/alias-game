package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.ApiVersion;
import game.alias.common.pagination.*;
import game.alias.player.domains.Player;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomMapper;
import game.alias.room.domains.dto.RoomDto;
import game.alias.room.domains.request.CreateRoomRequest;
import game.alias.player.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<RoomDto>createRoom(@Valid @RequestBody CreateRoomRequest request, @AuthenticationPrincipal AuthUser user){
        var room = service.create(request, user);
        var ownerPlayer = playerService.cashePlayer(user);
        var roomDto = mapper.toRoomDto(room, ownerPlayer);
        URI location = URI.create("/room/" + room.getId());
        return ResponseEntity.created(location).body(roomDto);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void>deleteRoom(@PathVariable UUID roomId, @AuthenticationPrincipal AuthUser user){
        service.delete(roomId, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<PaginationResult<RoomDto>>getRooms(
            Pageable pageable,
            @RequestParam(required = false) List<String> filter
    ){
        List<QueryFilter> filters = FilterParser.parse(filter);

        Pageable validatedPageable = QueryValidator.validatePageable(pageable, queryConfigProvider.getConfig());
        QueryValidator.validateFilters(filters, queryConfigProvider.getConfig());

        PaginationResult<Room> paginatedRooms = service.getRooms(validatedPageable, filters);
        Set<UUID> ownersId = paginatedRooms.getContent().stream().map(Room::getOwnerId).collect(Collectors.toSet());
        Map<UUID, Player> ownersById = playerService.loadExistingPlayers(ownersId).stream().collect(Collectors.toMap(Player::getId, Function.identity()));
        List<RoomDto> roomDtos = paginatedRooms.getContent().stream().map(room->mapper.toRoomDto(room,ownersById.get(room.getOwnerId()))).toList();

        PaginationResult<RoomDto> result =
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
    public ResponseEntity<RoomDto>joinRoom(@PathVariable UUID roomId, @AuthenticationPrincipal AuthUser user){
        Room room = service.joinRoom(roomId, user);
        Player currentPlayer = playerService.cashePlayer(user);
        RoomDto roomDto = mapper.toRoomDto(room, currentPlayer);

        return ResponseEntity.ok(roomDto);
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<RoomDto>leaveRoom(@PathVariable UUID roomId, @AuthenticationPrincipal AuthUser user){
        Room room = service.leaveRoom(roomId, user);

        // If user was owner and deleted room, room will no longer exist
        if (room.getPlayersId().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Player currentPlayer = playerService.cashePlayer(user);
        RoomDto roomDto = mapper.toRoomDto(room, currentPlayer);

        return ResponseEntity.ok(roomDto);
    }

    @GetMapping("/current")
    public ResponseEntity<RoomDto> getCurrentRoom(@AuthenticationPrincipal AuthUser user) {
        Optional<Room> roomOpt = service.findRoomByPlayer(user.getId());

        if (roomOpt.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Room room = roomOpt.get();
        var player = playerService.cashePlayer(user);
        RoomDto roomDto = mapper.toRoomDto(room, player);

        return ResponseEntity.ok(roomDto);
    }
}
