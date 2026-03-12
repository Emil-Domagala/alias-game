package game.alias.room;

import game.alias.common.pagination.QueryConfigBuilder;
import game.alias.common.pagination.QueryConfigModel;
import game.alias.room.domains.RoomStatus;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomQueryConfigProvider {
    public QueryConfigModel.QueryConfig getConfig() {
        return new QueryConfigBuilder()
                .defaultSort("playersCount", Sort.Direction.DESC)
                .sortField("name", "Room name")
                .sortField("playersCount", "Players")
                .sortField("maxPlayers", "Max players")
                .sortField("minPlayers", "Min players")
                .filter("status", QueryConfigModel.FilterType.SELECT, List.of(RoomStatus.WAITING.toString(), RoomStatus.FULL.toString(), RoomStatus.IN_GAME.toString()), List.of(QueryConfigModel.FilterOperator.EQ))
                .searchFields("name")
                .searchPlaceholder("Search by room name")
                .pageSizes(10,20,50)
                .build();
    }
}
