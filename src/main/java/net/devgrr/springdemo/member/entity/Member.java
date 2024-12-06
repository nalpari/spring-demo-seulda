package net.devgrr.springdemo.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.member.MemberRole;
import net.devgrr.springdemo.model.entity.BaseEntity;

@Getter
@Setter
@Builder
@Entity
@Table(name = "member")
@AllArgsConstructor
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MemberRole role;

  @Column(length = 1000)
  private String refreshToken;

  @JsonIgnore
  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
  private List<Board> boards;

  public Member() {}
}
