package net.devgrr.springdemo.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BoardRequest {
    private final Integer id;
    private final String title;
    private final String content;
}
