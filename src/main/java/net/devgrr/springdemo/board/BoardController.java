package net.devgrr.springdemo.board;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/board")
@RequiredArgsConstructor
@Tag(name = "BoardController", description = "게시판 API")
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(description = "게시글 목록을 조회한다.")
    @GetMapping("/list")
    public List<Board> selectBoard() {
        return boardService.selectBoard();
    }

    @Operation(description = "게시글을 조회한다.")
    @GetMapping("/{id}")
    public Board selectBoardById(@PathVariable("id") Integer id) throws BaseException {
        return boardService.selectBoardById(id);
    }

    @Operation(description = "게시글을 등록한다.")
    @PostMapping("")
    // 201
    public void insertBoard(@RequestBody BoardRequest req) {
//        return req.getId()+" insertBoard";
    }

    @Operation(description = "게시글을 수정한다.")
    @PutMapping("")
    // 200
    public void updateBoard(@RequestBody BoardRequest req) throws BaseException {
//        return req.getId()+" updateBoard";
    }

    @Operation(description = "게시글을 삭제한다.")
    @DeleteMapping("")
    // 204
    public void deleteBoard(@RequestBody BoardRequest req) throws BaseException {
//        return req.getId()+" deleteBoard";
    }

}