package net.devgrr.springdemo.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시글 응답")
public record BoardResponse(
    @Schema(description = "게시글 ID") Long id,
    @Schema(description = "게시글 제목") String title,
    @Schema(description = "게시글 내용") String content,
    @Schema(description = "작성자 ID") String writerId,
    @Schema(description = "작성자 이름") String writerName,
    @Schema(description = "게시글 추천 수") Integer likeCount,
    @Schema(description = "게시글 작성 일자") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
    @Schema(description = "게시글 수정 일자") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt) {}
