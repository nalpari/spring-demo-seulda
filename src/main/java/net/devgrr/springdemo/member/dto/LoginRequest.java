package net.devgrr.springdemo.member.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "필수값: userId", groups = MemberValidationGroup.loginGroup.class)
        String userId,
    @NotBlank(message = "필수값: password", groups = MemberValidationGroup.loginGroup.class)
        String password) {}
