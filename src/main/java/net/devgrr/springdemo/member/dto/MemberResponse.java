package net.devgrr.springdemo.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import net.devgrr.springdemo.member.MemberRole;

public record MemberResponse(
    Long id,
    String userId,
    String name,
    String email,
    MemberRole role,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt) {}
