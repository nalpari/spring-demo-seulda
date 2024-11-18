package net.devgrr.springdemo.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record MemberResponse(
    Long id,
    String userId,
    String name,
    String email,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt) {}
