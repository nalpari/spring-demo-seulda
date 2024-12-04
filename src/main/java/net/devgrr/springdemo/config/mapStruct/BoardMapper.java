package net.devgrr.springdemo.config.mapStruct;

import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {
  //  BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "member", target = "writer")
  Board toBoard(BoardRequest boardRequest, Member member);

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

  @Mapping(source = "board.writer.userId", target = "writerId")
  @Mapping(source = "board.writer.name", target = "writerName")
  BoardResponse toResponse(Board board);
}
