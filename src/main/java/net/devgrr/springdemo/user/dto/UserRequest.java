package net.devgrr.springdemo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank(message = "필수값: userId", groups = UserValidationGroup.createGroup.class)
        String userId,
    @NotBlank(message = "필수값: password", groups = UserValidationGroup.createGroup.class)
        String password,
    @NotBlank(message = "필수값: name", groups = UserValidationGroup.createGroup.class) String name,
    @NotBlank(message = "필수값: email", groups = UserValidationGroup.createGroup.class)
        @Email(message = "유효하지 않은 이메일 형식입니다.", groups = UserValidationGroup.createGroup.class)
        String email) {}
