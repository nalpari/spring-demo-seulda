package net.devgrr.springdemo.board.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "게시글 요청")
public record BoardRequest(
    @Schema(description = "게시글 ID")
        @NotNull(message = "필수값: id", groups = BoardValidationGroups.idGroup.class)
        @JsonView(BoardValidationGroups.idGroup.class)
        Integer id,
    @Schema(description = "게시글 제목")
        @NotBlank(message = "필수값: title", groups = BoardValidationGroups.articleGroup.class)
        @JsonView(BoardValidationGroups.articleGroup.class)
        String title,
    @Schema(description = "게시글 내용")
        @NotBlank(message = "필수값: content", groups = BoardValidationGroups.articleGroup.class)
        @JsonView(BoardValidationGroups.articleGroup.class)
        String content,
    @Schema(description = "태그") @JsonView(BoardValidationGroups.articleGroup.class)
        List<String> tag) {}
