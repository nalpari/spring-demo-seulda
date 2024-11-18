package net.devgrr.springdemo.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
    @NotBlank(message = "필수값: userId", groups = MemberValidationGroup.createGroup.class)
        String userId,
    @NotBlank(message = "필수값: password", groups = MemberValidationGroup.createGroup.class)
        String password,
    @NotBlank(message = "필수값: name", groups = MemberValidationGroup.createGroup.class) String name,
    @NotBlank(message = "필수값: email", groups = MemberValidationGroup.createGroup.class)
        @Email(message = "유효하지 않은 이메일 형식입니다.", groups = MemberValidationGroup.createGroup.class)
        String email) {}
