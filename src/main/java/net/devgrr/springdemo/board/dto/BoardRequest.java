package net.devgrr.springdemo.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardRequest(
        @NotNull(message = "필수값: id", groups = BoardValidationGroups.idGroup.class) Integer id,
        @NotBlank(message = "필수값: title", groups = BoardValidationGroups.articleGroup.class) String title,
        @NotBlank(message = "필수값: content", groups = BoardValidationGroups.articleGroup.class) String content
) {}
