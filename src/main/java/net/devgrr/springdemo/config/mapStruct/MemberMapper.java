package net.devgrr.springdemo.config.mapStruct;

import net.devgrr.springdemo.member.dto.MemberRequest;
import net.devgrr.springdemo.member.dto.MemberResponse;
import net.devgrr.springdemo.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {
  //  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Named("pwEncoder")
  static String pwEncoder(String password) {
    BCryptPasswordEncoder pe = new BCryptPasswordEncoder(); // interface <-> bean ..
    return pe.encode(password);
  }

  @Mapping(source = "password", target = "password", qualifiedByName = "pwEncoder")
  Member toUser(MemberRequest userRequest);

  MemberResponse toResponse(Member member);
}
