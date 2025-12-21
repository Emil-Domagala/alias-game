package game.alias.common.message;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessagesMapper {

    MessageDto toMessageDto(Message msg);
}
