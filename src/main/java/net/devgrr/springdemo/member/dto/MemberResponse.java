package net.devgrr.springdemo.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "회원 응답")
public record MemberResponse(
    @Schema(description = "고유 ID") Long id,
    @Schema(description = "회원 ID") String userId,
    @Schema(description = "회원 이름") String name,
    @Schema(description = "회원 이메일") String email,
    @Schema(description = "회원 생성 일자") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
    @Schema(description = "회원 수정 일자") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt) {}
