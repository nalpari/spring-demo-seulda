package net.devgrr.springdemo.comment;

import java.util.List;
import net.devgrr.springdemo.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

  List<Comment> findAllByBoardId(Integer boardId);
}
