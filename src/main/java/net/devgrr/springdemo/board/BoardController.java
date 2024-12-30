package net.devgrr.springdemo.board;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import net.devgrr.springdemo.board.dto.BoardValidationGroups;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.mapStruct.BoardMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Tag(name = "BoardController", description = "게시판 API")
@RestController
public class BoardController {

  private final BoardService boardService;
  private final BoardMapper boardMapper;

  @Operation(description = "게시글 목록을 조회한다. 검색 조건이 있다면 조건 키워드가 포함된 게시글을 조회한다.")
  @GetMapping
  public List<BoardResponse> selectBoard(
      @RequestParam(value = "title", required = false) String title,
      @RequestParam(value = "content", required = false) String content,
      @RequestParam(value = "tag", required = false) String tag) {
    List<Board> boards =
        (title != null && !title.trim().isEmpty())
                || (content != null && !content.trim().isEmpty())
                || (tag != null && !tag.trim().isEmpty())
            ? boardService.selectBoardByKeywords(title, content, tag)
            : boardService.selectBoard();
    return boards.stream().map(boardMapper::toResponse).collect(Collectors.toList());
  }

  @Operation(description = "게시글을 조회한다.")
  @GetMapping("/{id}")
  public BoardResponse selectBoardById(@PathVariable("id") Integer id) throws BaseException {
    Board board = boardService.selectBoardById(id);
    return boardMapper.toResponse(board);
  }

  @Operation(description = "게시글을 등록한다.")
  @JsonView(BoardValidationGroups.articleGroup.class)
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BoardResponse insertBoard(
      @Validated(BoardValidationGroups.articleGroup.class) @RequestBody BoardRequest req,
      Principal principal) {
    Board board = boardService.insertBoard(req, principal.getName());
    return boardMapper.toResponse(board);
  }

  @Operation(description = "게시글을 수정한다.")
  @PutMapping
  public void updateBoard(
      @Validated(BoardValidationGroups.articleGroup.class) @RequestBody BoardRequest req,
      Principal principal)
      throws BaseException {
    boardService.updateBoard(req, principal.getName());
  }

  @Operation(description = "게시글을 삭제한다.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBoard(@PathVariable("id") Integer id, Principal principal)
      throws BaseException {
    boardService.deleteBoard(id, principal.getName());
  }

  @Operation(description = "게시글을 추천 또는 취소한다.")
  @JsonView(BoardValidationGroups.idGroup.class)
  @PutMapping("/like")
  public void likeBoard(
      @Validated(BoardValidationGroups.idGroup.class) @RequestBody BoardRequest req,
      Principal principal)
      throws BaseException {
    boardService.likeBoard(req.id(), principal.getName());
  }
}
