package net.devgrr.springdemo.config.mapStruct;

import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {
  //  BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

  Board toBoard(BoardRequest boardRequest);

  @Mapping(target = "id", ignore = true)
  @Mapping(
      target = "title",
      source = "title",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(
      target = "content",
      source = "content",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Board updateBoardMapper(BoardRequest boardRequest, @MappingTarget Board board);
}
