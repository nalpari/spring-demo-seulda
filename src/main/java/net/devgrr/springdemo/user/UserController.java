package net.devgrr.springdemo.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.user.dto.UserRequest;
import net.devgrr.springdemo.user.dto.UserResponse;
import net.devgrr.springdemo.user.dto.UserValidationGroup;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 API")
@RestController
public class UserController {

  private final UserService userService;

  @Operation(description = "사용자를 조회한다.")
  @GetMapping("/{userId}")
  public UserResponse selectUserInfo(@PathVariable("userId") String userId) throws BaseException {
    return userService.selectUserInfo(userId);
  }

  @Operation(description = "사용자를 생성한다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse insertUser(
      @Validated(UserValidationGroup.createGroup.class) @RequestBody UserRequest req)
      throws BaseException {
    return userService.insertUser(req);
  }
}
