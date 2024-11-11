package net.devgrr.springdemo.board;


import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public List<Board> selectBoard() {
        return boardRepository.findAll();
    }


    public Board selectBoardById(Integer id) throws BaseException {
        return boardRepository.findById(id).orElseThrow(() -> new BaseException(ErrorCode.INVALID_INPUT_VALUE, "Board not found with id: " + id));
    }


    public void insertBoard(BoardRequest req) {
//        Board board = new Board();
//        board.setTitle(req.getTitle());
//        board.setContent(req.getContent());
//        boardRepository.save(board);
    }


    public void updateBoard(BoardRequest req) throws BaseException {
//        Board board = selectBoardById(req.getId());
//        if (req.getTitle() != null) {
//            board.setTitle(req.getTitle());
//        }
//        if (req.getContent() != null) {
//            board.setContent(req.getContent());
//        }
//        board.setUpdatedAt(LocalDateTime.now());
//        boardRepository.save(board);
    }


    public void deleteBoard(BoardRequest req) throws BaseException {
//        Board board = selectBoardById(req.getId());
//        boardRepository.delete(board);
    }
}
