package net.devgrr.springdemo.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardValidationGroups;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
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

  @Operation(description = "게시글 목록을 조회한다.")
  @GetMapping
  public List<Board> selectBoard() {
    return boardService.selectBoard();
  }

  @Operation(description = "게시글을 조회한다.")
  @GetMapping("/{id}")
  public Board selectBoardById(@PathVariable("id") Integer id) throws BaseException {
    return boardService.selectBoardById(id);
  }

  @Operation(description = "게시글을 등록한다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Board insertBoard(
      @Validated(BoardValidationGroups.articleGroup.class) @RequestBody BoardRequest req) {
    return boardService.insertBoard(req);
  }

  @Operation(description = "게시글을 수정한다.")
  @PutMapping
  public void updateBoard(
      @Validated(BoardValidationGroups.idGroup.class) @RequestBody BoardRequest req)
      throws BaseException {
    boardService.updateBoard(req);
  }

  @Operation(description = "게시글을 삭제한다.")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBoard(@PathVariable("id") Integer id) throws BaseException {
    boardService.deleteBoard(id);
  }
}
