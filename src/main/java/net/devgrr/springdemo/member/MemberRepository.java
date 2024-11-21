package net.devgrr.springdemo.member;

import java.util.Optional;
import net.devgrr.springdemo.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
  Optional<Member> findByUserId(String userId);

  boolean existsByUserId(String userId);

  boolean existsByEmail(String email);
}
