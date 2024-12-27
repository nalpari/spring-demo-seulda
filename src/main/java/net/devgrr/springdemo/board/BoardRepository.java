package net.devgrr.springdemo.board;

import java.util.List;
import net.devgrr.springdemo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {

  List<Board> findAllByTagContaining(String tag);
}
