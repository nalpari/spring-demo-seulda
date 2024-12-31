package net.devgrr.springdemo.comment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.BoardService;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.comment.dto.CommentRequest;
import net.devgrr.springdemo.comment.dto.CommentResponse;
import net.devgrr.springdemo.comment.entity.Comment;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import net.devgrr.springdemo.config.mapStruct.CommentMapper;
import net.devgrr.springdemo.member.MemberService;
import net.devgrr.springdemo.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  private final BoardService boardService;
  private final MemberService memberService;

  public List<Comment> selectCommentByBoardId(Integer boardId) {
    return commentRepository.findAllByBoardId(boardId);
  }

  public List<CommentResponse> selectCommentByBoardIdNested(Integer boardId) {
    List<Comment> comments = selectCommentByBoardId(boardId);
    return comments.stream()
        .filter(comment -> comment.getParentCommentId() == null)
        .map(
            rootComment -> {
              List<CommentResponse> childComments =
                  comments.stream()
                      .filter(
                          comment ->
                              Objects.equals(comment.getParentCommentId(), rootComment.getId()))
                      .map(commentMapper::toResponse)
                      .collect(Collectors.toList());
              return commentMapper.toResponseWithChildren(rootComment, childComments);
            })
        .collect(Collectors.toList());
  }

  public Comment selectCommentById(Integer id) throws BaseException {
    return commentRepository
        .findById(id)
        .orElseThrow(
            () ->
                new BaseException(
                    ErrorCode.INVALID_INPUT_VALUE, "Comment not found with id: " + id));
  }

  @Transactional
  public Comment insertComment(CommentRequest req, String userId) throws BaseException {
    Member member = memberService.selectUserByUserId(userId);
    Board board = boardService.selectBoardById(req.boardId());
    Comment comment = commentMapper.toComment(req, board, member);
    commentRepository.save(comment);
    return comment;
  }

  @Transactional
  public void updateComment(CommentRequest req, String userId) throws BaseException {
    Comment comment = selectCommentById(req.id());
    if (!comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "수정 권한이 없습니다.");
    }
    Comment updComment = commentMapper.updateCommentMapper(req, comment);
    commentRepository.save(updComment);
  }

  @Transactional
  public void deleteComment(Integer id, String userId) throws BaseException {
    Comment comment = selectCommentById(id);
    if (!comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "삭제 권한이 없습니다.");
    }
    commentRepository.delete(comment);
  }

  @Transactional
  public void likeComment(Integer id, String userId) throws BaseException {
    Comment comment = selectCommentById(id);

    if (comment.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "본인 댓글에는 추천할 수 없습니다.");
    }

    if (comment.getLikes().stream().anyMatch(member -> member.getUserId().equals(userId))) {
      // unlike
      comment.getLikes().removeIf(member -> member.getUserId().equals(userId));
    } else {
      // like
      comment.getLikes().add(memberService.selectUserByUserId(userId));
    }
    commentRepository.save(comment);
  }
}
