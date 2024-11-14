package net.devgrr.springdemo.board;

import net.devgrr.springdemo.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Integer> {}
