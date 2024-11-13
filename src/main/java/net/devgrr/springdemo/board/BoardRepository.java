package net.devgrr.springdemo.board;

import java.util.List;
import net.devgrr.springdemo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {
  Board findByTitle(String title);

  Board findByTitleAndContent(String title, String content);

  List<Board> findByContentLike(String content);
}
