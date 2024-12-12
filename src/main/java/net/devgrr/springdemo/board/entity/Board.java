package net.devgrr.springdemo.board.entity;

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
@AllArgsConstructor
public class Board extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String title;

  private String content;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member writer;

  @ManyToMany private Set<Member> likes;

  public Board() {}
}
