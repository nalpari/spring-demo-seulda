package net.devgrr.springdemo.user;

import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.config.exception.ErrorCode;
import net.devgrr.springdemo.config.mapStruct.UserMapper;
import net.devgrr.springdemo.user.dto.UserRequest;
import net.devgrr.springdemo.user.dto.UserResponse;
import net.devgrr.springdemo.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired private UserMapper userMapper;

  public User selectUserByUserId(String userId) {
    return userRepository.findByUserId(userId).orElse(null);
  }

  public UserResponse selectUserInfo(String userId) throws BaseException {
    User user = selectUserByUserId(userId);
    if (user == null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "존재하지 않는 ID 입니다.");
    }
    return userMapper.toResponse(user);
  }

  public User selectUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  @Transactional
  public UserResponse insertUser(UserRequest req) throws BaseException {
    if (selectUserByUserId(req.userId()) != null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 ID 입니다.");
    } else if (selectUserByEmail(req.email()) != null) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, "이미 존재하는 Email 입니다.");
    }

    User user = userMapper.toUser(req);
    try {
      userRepository.save(user);
    } catch (Exception e) {
      throw new BaseException(ErrorCode.INVALID_INPUT_VALUE, e.getMessage());
    }
    return userMapper.toResponse(user);
  }
}
