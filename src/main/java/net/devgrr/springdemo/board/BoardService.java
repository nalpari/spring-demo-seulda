package net.devgrr.springdemo.board;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import net.devgrr.springdemo.config.mapStruct.BoardMapper;
import net.devgrr.springdemo.member.MemberService;
import net.devgrr.springdemo.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BoardService {

  private final BoardRepository boardRepository;
  private final MemberService memberService;
  private final BoardMapper boardMapper;

  @Transactional(readOnly = true)
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

  public List<Board> selectBoardByTag(String tag) {
    return boardRepository.findAllByTagContaining(tag);
  }

  @Transactional
  public Board insertBoard(BoardRequest req, String userId) {
    Member member = memberService.selectUserByUserId(userId);
    Board board = boardMapper.toBoard(req, member);
    boardRepository.save(board);
    return board;
  }

  public void updateBoard(BoardRequest req, String userId) throws BaseException {
    Board board = selectBoardById(req.id());
    if (!board.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "수정 권한이 없습니다.");
    }
    Board updBoard = boardMapper.updateBoardMapper(req, board);
    boardRepository.save(updBoard);
  }

  public void deleteBoard(Integer id, String userId) throws BaseException {
    Board board = selectBoardById(id);
    if (!board.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "삭제 권한이 없습니다.");
    }
    boardRepository.delete(board);
  }

  public void likeBoard(Integer id, String userId) throws BaseException {
    Board board = selectBoardById(id);

    if (board.getWriter().getUserId().equals(userId)) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "본인 게시글은 추천할 수 없습니다.");
    }

    if (board.getLikes().stream().anyMatch(member -> member.getUserId().equals(userId))) {
      // unlike
      board.getLikes().removeIf(member -> member.getUserId().equals(userId));
      boardRepository.save(board);
    } else {
      // like
      board.getLikes().add(memberService.selectUserByUserId(userId));
      boardRepository.save(board);
    }
  }
}
