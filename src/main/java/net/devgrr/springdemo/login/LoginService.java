package net.devgrr.springdemo.login;

import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.member.MemberRepository;
import net.devgrr.springdemo.member.entity.Member;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Member member =
        memberRepository
            .findByUserId(userId)
            .orElseThrow(() -> new UsernameNotFoundException("Not found user"));

    return User.builder()
        .username(member.getUserId())
        .password(member.getPassword())
        .roles(member.getRole().name())
        .build();
  }
}
