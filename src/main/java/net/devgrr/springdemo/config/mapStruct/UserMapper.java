package net.devgrr.springdemo.config.mapStruct;

import net.devgrr.springdemo.user.dto.UserRequest;
import net.devgrr.springdemo.user.dto.UserResponse;
import net.devgrr.springdemo.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
  //  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Named("pwEncoder")
  static String pwEncoder(String password) {
    BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // interface <-> bean ..
    return pe.encode(password);
  }

  @Mapping(source = "password", target = "password", qualifiedByName = "pwEncoder")
  User toUser(UserRequest userRequest);

  UserResponse toResponse(User user);
}
