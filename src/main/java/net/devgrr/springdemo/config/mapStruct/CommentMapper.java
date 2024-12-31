package net.devgrr.springdemo.config.mapStruct;

import java.util.List;
import java.util.Set;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.comment.dto.CommentRequest;
import net.devgrr.springdemo.comment.dto.CommentResponse;
import net.devgrr.springdemo.comment.entity.Comment;
import net.devgrr.springdemo.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

// import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  //  CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

  @Named("likeToCount")
  static int likeToCount(Set<Member> likeMember) {
    return likeMember != null ? likeMember.size() : 0;
  }

  @Mapping(source = "comment.writer.userId", target = "writerId")
  @Mapping(source = "comment.writer.name", target = "writerName")
  @Mapping(source = "comment.board.id", target = "boardId")
  @Mapping(source = "comment.likes", target = "likeCount", qualifiedByName = "likeToCount")
  CommentResponse toResponse(Comment comment);

  @Mapping(source = "comment.writer.userId", target = "writerId")
  @Mapping(source = "comment.writer.name", target = "writerName")
  @Mapping(source = "comment.board.id", target = "boardId")
  @Mapping(source = "comment.likes", target = "likeCount", qualifiedByName = "likeToCount")
  @Mapping(source = "childComments", target = "childComment")
  CommentResponse toResponseWithChildren(Comment comment, List<CommentResponse> childComments);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "board", ignore = true)
  @Mapping(target = "parentCommentId", ignore = true)
  @Mapping(
      source = "content",
      target = "content",
      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Comment updateCommentMapper(CommentRequest commentRequest, @MappingTarget Comment comment);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "likes", ignore = true)
  @Mapping(source = "commentRequest.content", target = "content")
  @Mapping(source = "board", target = "board")
  @Mapping(source = "member", target = "writer")
  Comment toComment(CommentRequest commentRequest, Board board, Member member);
}
