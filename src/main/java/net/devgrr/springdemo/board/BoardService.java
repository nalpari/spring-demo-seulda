package net.devgrr.springdemo.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import net.devgrr.springdemo.config.mapStruct.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {

  private final BoardRepository boardRepository;

  @Autowired private BoardMapper boardMapper;

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

  public Board insertBoard(BoardRequest req) {
    Board board = boardMapper.toBoard(req);
    boardRepository.save(board);
    return board;
  }

  public void updateBoard(BoardRequest req) throws BaseException {
    Board board = selectBoardById(req.id());
    Board updBoard = boardMapper.updateBoardMapper(req, board);
    boardRepository.save(updBoard);
  }

  public void deleteBoard(Integer id) throws BaseException {
    Board board = selectBoardById(id);
    boardRepository.delete(board);
  }
}
