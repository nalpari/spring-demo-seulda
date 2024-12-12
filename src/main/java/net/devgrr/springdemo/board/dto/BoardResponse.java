package net.devgrr.springdemo.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record BoardResponse(
    Long id,
    String title,
    String content,
    String writerId,
    String writerName,
    Integer likeCount,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createdAt,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime updatedAt) {}
