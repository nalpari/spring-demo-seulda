package net.devgrr.springdemo.board;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {

  private final BoardRepository boardRepository;

  public List<Board> selectBoard() {
    return boardRepository.findAll();
  }

  public Board selectBoardById(Integer id) throws BaseException {
    return boardRepository
        .findById(id)
        .orElseThrow(
            () ->
                new BaseException(ErrorCode.INVALID_INPUT_VALUE, "Board not found with id: " + id));
  }

  public Integer insertBoard(BoardRequest req) {
    Board board = new Board();
    board.setTitle(req.title());
    board.setContent(req.content());
    board.setCreatedAt(LocalDateTime.now());
    return boardRepository.save(board).getId();
  }

  public void updateBoard(BoardRequest req) throws BaseException {
    Board board = selectBoardById(req.id());
    if (req.title() != null) {
      board.setTitle(req.title());
    }
    if (req.content() != null) {
      board.setContent(req.content());
    }
    board.setUpdatedAt(LocalDateTime.now());
    boardRepository.save(board);
  }

  public void deleteBoard(Integer id) throws BaseException {
    Board board = selectBoardById(id);
    boardRepository.delete(board);
  }
}
