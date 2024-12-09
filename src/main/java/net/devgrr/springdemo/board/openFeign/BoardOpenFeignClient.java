package net.devgrr.springdemo.board.openFeign;

import feign.Response;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "boardClient", url = "http://localhost:8080/api/v1/board")
public interface BoardOpenFeignClient {

  @GetMapping("/{id}")
  Response getBoardById(@PathVariable("id") Integer id);

  @PostMapping
  BoardResponse createBoard(@RequestBody BoardRequest req);
}
