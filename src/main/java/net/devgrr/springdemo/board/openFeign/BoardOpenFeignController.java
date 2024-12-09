package net.devgrr.springdemo.board.openFeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import net.devgrr.springdemo.board.dto.BoardValidationGroups;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Validated
@RequestMapping("/api/v1/board/openfeign")
@RequiredArgsConstructor
@Tag(name = "BoardOpenFeignController", description = "게시판 API의 전달 API")
@RestController
public class BoardOpenFeignController {

  private final ObjectMapper objectMapper;
  private final BoardOpenFeignClient boardOpenFeignClient;

  @Operation(description = "게시글을 조회해온다.")
  @GetMapping("/{id}")
  public BoardResponse selectBoardById(@PathVariable("id") Integer id)
      throws ResponseStatusException, IOException {
    Response response = boardOpenFeignClient.getBoardById(id);
    if (response.status() != 200) {
      throw new ResponseStatusException(
          Objects.requireNonNull(HttpStatus.resolve(response.status())),
          objectMapper.readTree(response.body().asInputStream()).get("message").asText());
    }
    return objectMapper.readValue(response.body().asInputStream(), BoardResponse.class);
  }

  @Operation(description = "게시글을 등록해온다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BoardResponse insertBoard(
      @Validated(BoardValidationGroups.articleGroup.class) @RequestBody BoardRequest req) {
    return boardOpenFeignClient.createBoard(req);
  }
}
