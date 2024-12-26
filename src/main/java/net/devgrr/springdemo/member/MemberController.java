package net.devgrr.springdemo.member;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.member.dto.MemberRequest;
import net.devgrr.springdemo.member.dto.MemberResponse;
import net.devgrr.springdemo.member.dto.MemberValidationGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 API")
@RestController
public class MemberController {

  private final MemberService memberService;

  @Operation(description = "사용자를 조회한다.")
  @GetMapping("/{userId}")
  public MemberResponse selectUserInfo(@PathVariable("userId") String userId) throws BaseException {
    return memberService.selectUserInfo(userId);
  }

  @Operation(description = "사용자를 생성한다.")
  @JsonView(MemberValidationGroup.createGroup.class)
  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public MemberResponse insertUser(
      @Validated(MemberValidationGroup.createGroup.class) @RequestBody MemberRequest req)
      throws BaseException {
    return memberService.insertUser(req);
  }
}
