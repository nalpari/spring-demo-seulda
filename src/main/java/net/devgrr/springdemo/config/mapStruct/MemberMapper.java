package net.devgrr.springdemo.config.mapStruct;

import net.devgrr.springdemo.member.MemberRole;
import net.devgrr.springdemo.member.dto.MemberRequest;
import net.devgrr.springdemo.member.dto.MemberResponse;
import net.devgrr.springdemo.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

  @Named("toMemberRole")
  static MemberRole toMemberRole(String role) {
    if (role == null) {
      return MemberRole.USER;
    }
    String r = role.toUpperCase();
    if (r.equals("ADMIN")) {
      return MemberRole.ADMIN;
    } else {
      return MemberRole.USER;
    }
  }

  @Mapping(source = "password", target = "password", qualifiedByName = "pwEncoder")
  @Mapping(source = "role", target = "role", qualifiedByName = "toMemberRole")
  Member toMember(MemberRequest memberRequest);

  MemberResponse toResponse(Member member);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "userId", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "refreshToken", source = "refreshToken")
  Member updateMemberRefreshToken(Member updateMember, @MappingTarget Member member);
}
