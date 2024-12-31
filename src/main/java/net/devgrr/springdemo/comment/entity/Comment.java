package net.devgrr.springdemo.comment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.member.entity.Member;
import net.devgrr.springdemo.model.entity.BaseEntity;

@Getter
@Setter
@Builder
@Entity
@Table(name = "comment")
@Schema(description = "댓글 엔티티")
@AllArgsConstructor
public class Comment extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "댓글 ID")
  private Integer id;

  @Schema(description = "부모 댓글 ID")
  private Integer parentCommentId;

  @Column(nullable = false)
  @Schema(description = "댓글 내용")
  private String content;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  @Schema(description = "작성자")
  private Member writer;

  @ManyToMany
  @Schema(description = "추천")
  private Set<Member> likes;

  @ManyToOne
  @JoinColumn(name = "board_id", nullable = false)
  @Schema(description = "게시글")
  private Board board;

  public Comment() {}
}
