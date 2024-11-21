package net.devgrr.springdemo.member;

import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import net.devgrr.springdemo.config.mapStruct.MemberMapper;
import net.devgrr.springdemo.member.dto.MemberRequest;
import net.devgrr.springdemo.member.dto.MemberResponse;
import net.devgrr.springdemo.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;

  @Autowired private MemberMapper memberMapper;

  public Member selectUserByUserId(String userId) {
    return memberRepository.findByUserId(userId).orElse(null);
  }

  public MemberResponse selectUserInfo(String userId) throws BaseException {
    Member member = selectUserByUserId(userId);
    if (member == null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "존재하지 않는 ID 입니다.");
    }
    return memberMapper.toResponse(member);
  }

  public Member selectUserByEmail(String email) {
    return memberRepository.findByEmail(email).orElse(null);
  }

  @Transactional
  public MemberResponse insertUser(MemberRequest req) throws BaseException {
    if (selectUserByUserId(req.userId()) != null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 ID 입니다.");
    } else if (selectUserByEmail(req.email()) != null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 Email 입니다.");
    }

    Member member = memberMapper.toMember(req);
    try {
      memberRepository.save(member);
    } catch (Exception e) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
    }
    return memberMapper.toResponse(member);
  }
}
