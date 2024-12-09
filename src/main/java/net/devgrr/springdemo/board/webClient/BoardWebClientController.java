package net.devgrr.springdemo.board.webClient;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import net.devgrr.springdemo.board.dto.BoardValidationGroups;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.mapStruct.BoardMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Validated
@RequestMapping("/api/v1/board/webclient")
@RequiredArgsConstructor
@Tag(name = "BoardWebClientController", description = "게시판 API의 전달 API")
@RestController
public class BoardWebClientController {

  private final BoardMapper boardMapper;
  private final WebClient webClient;

  private final String boardUrl = "http://localhost:8080/api/v1/board";

  @Operation(description = "게시글을 조회해온다.")
  @GetMapping("/{id}")
  public BoardResponse selectBoardById(@PathVariable("id") Integer id)
      throws ResponseStatusException {
    String requestUrl = boardUrl + "/" + id;
    Board board =
        webClient
            .get()
            .uri(requestUrl)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse ->
                    clientResponse
                        .createException()
                        .flatMap(
                            error ->
                                Mono.error(
                                    new ResponseStatusException(
                                        clientResponse.statusCode(), error.getMessage()))))
            .bodyToMono(Board.class)
            .block();
    return boardMapper.toResponse(board);
  }

  @Operation(description = "게시글을 등록해온다.")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BoardResponse insertBoard(
      @Validated(BoardValidationGroups.articleGroup.class) @RequestBody BoardRequest req) {
    Board board =
        webClient
            .post()
            .uri(boardUrl)
            .bodyValue(req)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                clientResponse ->
                    clientResponse
                        .createException()
                        .flatMap(
                            error ->
                                Mono.error(
                                    new ResponseStatusException(
                                        clientResponse.statusCode(), error.getMessage()))))
            .bodyToMono(Board.class)
            .block();
    return boardMapper.toResponse(board);
  }
}
