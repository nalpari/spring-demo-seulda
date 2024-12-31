package net.devgrr.springdemo.comment;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.comment.dto.CommentRequest;
import net.devgrr.springdemo.comment.dto.CommentResponse;
import net.devgrr.springdemo.comment.dto.CommentValidationGroups;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.mapStruct.CommentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "CommentController", description = "댓글 API")
@RestController
public class CommentController {

  private final CommentService commentService;
  private final CommentMapper commentMapper;

  @Operation(description = "댓글 목록을 조회한다.")
  @GetMapping("/{boardId}")
  public List<CommentResponse> getCommentByBoard(
      @PathVariable("boardId") @Parameter(description = "게시글 ID") Integer boardId,
      @RequestParam(value = "nested", required = false, defaultValue = "false")
          @Parameter(description = "중첩구조 여부")
          Boolean nested) {
    return nested
        ? commentService.selectCommentByBoardIdNested(boardId)
        : commentService.selectCommentByBoardId(boardId).stream()
            .map(commentMapper::toResponse)
            .collect(Collectors.toList());
  }

  @Operation(description = "댓글을 생성한다.")
  @JsonView(CommentValidationGroups.createGroup.class)
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse createComment(
      @Validated(CommentValidationGroups.createGroup.class) @RequestBody CommentRequest req,
      Principal principal)
      throws BaseException {
    return commentMapper.toResponse(commentService.insertComment(req, principal.getName()));
  }

  @Operation(description = "댓글을 수정한다.")
  @JsonView(CommentValidationGroups.updateGroup.class)
  @PutMapping
  public void updateComment(
      @Validated(CommentValidationGroups.updateGroup.class) @RequestBody CommentRequest req,
      Principal principal)
      throws BaseException {
    commentService.updateComment(req, principal.getName());
  }

  @Operation(description = "댓글을 삭제한다.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(
      @PathVariable("id") @Parameter(description = "댓글 ID") Integer id, Principal principal)
      throws BaseException {
    commentService.deleteComment(id, principal.getName());
  }

  @Operation(description = "댓글을 추천 또는 취소한다.")
  @JsonView(CommentValidationGroups.idGroup.class)
  @PutMapping("/like")
  public void likeComment(
      @Validated(CommentValidationGroups.idGroup.class) @RequestBody CommentRequest req,
      Principal principal)
      throws BaseException {
    commentService.likeComment(req.id(), principal.getName());
  }
}
