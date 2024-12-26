package net.devgrr.springdemo.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "회원 엔티티")
@AllArgsConstructor
public class Member extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "고유 ID")
  private Long id;

  @Column(nullable = false, unique = true)
  @Schema(description = "회원 ID")
  private String userId;

  @Column(nullable = false)
  @Schema(description = "비밀번호")
  private String password;

  @Column(nullable = false)
  @Schema(description = "이름")
  private String name;

  @Column(nullable = false, unique = true)
  @Schema(description = "이메일")
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(description = "권한")
  private MemberRole role;

  @Column(length = 1000)
  @Schema(description = "인증 토큰")
  private String refreshToken;

  @JsonIgnore
  @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
  @Schema(description = "작성한 게시글")
  private List<Board> boards;

  public Member() {}
}
