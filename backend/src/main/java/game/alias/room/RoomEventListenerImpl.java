package game.alias.room;

import game.alias.common.WebSocketDestinations;
import game.alias.room.domains.Room;
import game.alias.room.domains.RoomMapper;
import game.alias.room.event.*;
import game.alias.player.PlayerService;
import game.alias.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomEventListenerImpl implements RoomEventListener{
    private final RoomService roomService;;
    private final PlayerService playerService;
    private final TeamService teamService;
    private final SimpMessagingTemplate template;
    private final RoomMapper roomMapper;
    private final RoomWsMapper roomWsMapper;


    @Override
    @EventListener(RoomPlayerJoinedEvent.class)
    public void onPlayerJoined(RoomPlayerJoinedEvent event) {
        Room room = roomService.loadRoomOrThrow(event.roomId());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());
        var players = playerService.loadExistingPlayers(Set.copyOf(room.getPlayersId()));
        var teams = teamService.findByRoom(room.getId());

        log.info("Player joined room. roomId={}, playerId={}", event.roomId(), event.userId());

        var roomStateDto = roomMapper.toState(room, owner, List.copyOf(players), List.copyOf(teams));

        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()),roomWsMapper.toRoomUpdated(roomStateDto));
    }

    @Override
    @EventListener(RoomPlayerLeftEvent.class)
    public void onPlayerLeft(RoomPlayerLeftEvent event) {
        Room room = roomService.loadRoomOrThrow(event.roomId());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());
        var players = playerService.loadExistingPlayers(Set.copyOf(room.getPlayersId()));
        var teams = teamService.findByRoom(room.getId());

        log.info("Player left room. roomId={}, playerId={}", event.roomId(), event.userId());

        var roomStateDto = roomMapper.toState(room, owner, List.copyOf(players), List.copyOf(teams));
        var roomSummaryDto = roomMapper.toRoomSummaryDto(room, owner);

        template.convertAndSend(WebSocketDestinations.roomTopic(room.getId()), roomWsMapper.toRoomUpdated(roomStateDto));
        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY, roomWsMapper.toRoomDeleted(roomSummaryDto));
    }

    @Override
    @EventListener(RoomCreatedEvent.class)
    public void onRoomCreated(RoomCreatedEvent event) {
        Room room = roomService.loadRoomOrThrow(event.roomId());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());
        var players = playerService.loadExistingPlayers(Set.copyOf(room.getPlayersId()));
        var teams = teamService.findByRoom(room.getId());

        log.info("Room created. roomId={}, ownerId={}, maxPlayers={}, minPlayers={}", room.getId(), room.getOwnerId(), room.getMaxPlayers(), room.getMinPlayers());

        var roomStateDto = roomMapper.toState(room, owner, List.copyOf(players), List.copyOf(teams));

        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY,roomWsMapper.toRoomUpdated(roomStateDto));
    }

    @Override
    @EventListener(RoomDeletedEvent.class)
    public void onRoomDeleted(RoomDeletedEvent event) {
        Room room = roomService.loadRoomOrThrow(event.roomId());
        var owner = playerService.loadExistingPlayer(room.getOwnerId());

        log.info("Room deleted. roomId={}", event.roomId());

        var roomSummaryDto = roomMapper.toRoomSummaryDto(room, owner);

        template.convertAndSend(WebSocketDestinations.roomTopic(event.roomId()),roomWsMapper.toRoomDeleted(roomSummaryDto));
        template.convertAndSend(WebSocketDestinations.TOPIC_LOBBY,roomWsMapper.toRoomDeleted(roomSummaryDto));
    }
}
