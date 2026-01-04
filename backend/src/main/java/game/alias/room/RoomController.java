package game.alias.room;

import game.alias.auth.AuthUser;
import game.alias.common.ApiVersion;
import game.alias.common.pagination.PaginationRequest;
import game.alias.common.pagination.PaginationResult;
import game.alias.player.domains.Player;
import game.alias.player.domains.PlayerMapper;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomMapper;
import game.alias.room.domains.dto.RoomDto;
import game.alias.room.domains.request.CreateRoomRequest;
import game.alias.player.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final PlayerMapper playerMapper;

    @PostMapping("/create")
    public ResponseEntity<RoomDto>createRoom(@Valid @RequestBody CreateRoomRequest request, @AuthenticationPrincipal AuthUser user){
        var room = service.create(request, user);
        var player = playerService.cashePlayer(user);
        var roomDto = mapper.toRoomDto(room, player);
        URI location = URI.create("/room/" + room.getId());
        return ResponseEntity.created(location).body(roomDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void>deleteRoom(@PathVariable UUID id, @AuthenticationPrincipal AuthUser user){
        return null;
    }

    @GetMapping()
    public ResponseEntity<PaginationResult<RoomDto>>getRooms(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) Sort.Direction direction
    ){
        if(sortField == null || !RoomDto.ALLOWED_SORT_FIELDS.contains(sortField)){
            sortField = RoomDto.DEFAULT_SORT_FIELD;
            direction = Sort.Direction.ASC;
        }

        final var paginationRequest = new PaginationRequest(page, size, sortField, direction);

        PaginationResult<Room> paginatedRooms = service.getRooms(paginationRequest);

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
        return null;
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<RoomDto>leaveRoom(@PathVariable UUID roomId, @AuthenticationPrincipal AuthUser user){
        return null;
    }
}
