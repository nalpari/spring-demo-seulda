package net.devgrr.springdemo.board.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.devgrr.springdemo.member.entity.Member;
import net.devgrr.springdemo.model.entity.BaseEntity;

@Getter
@Setter
@Builder
@Entity
@Table(name = "board")
@Schema(description = "게시글 엔티티")
@AllArgsConstructor
public class Board extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "게시글 ID")
  private Integer id;

  @Column(nullable = false)
  @Schema(description = "게시글 제목")
  private String title;

  @Column(nullable = false)
  @Schema(description = "게시글 내용")
  private String content;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  @Schema(description = "작성자")
  private Member writer;

  @ManyToMany
  @Schema(description = "추천")
  private Set<Member> likes;

  @Schema(description = "태그")
  private String tag;

  public Board() {}
}
