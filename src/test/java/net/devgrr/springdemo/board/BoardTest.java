package net.devgrr.springdemo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.entity.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
public class BoardTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private BoardRepository boardRepository;

  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("게시글 목록 조회")
  void selectBoardTest() throws Exception {
    String url = "/api/v1/board/list";
    MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();
    String res = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
  }

  @Test
  @DisplayName("게시글 조회")
  void selectBoardByIdTest() throws Exception {
    // given
    int id = 1;
    String url = "/api/v1/board/" + id;

    // when
    MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

    // then
    Board resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), Board.class);

    assertEquals(id, resData.getId());
  }

  @Test
  @DisplayName("게시글 생성")
  void insertBoardTest() throws Exception {
    // given
    String url = "/api/v1/board";
    String title = "게시글 생성 테스트의 제목";
    String content = "내용입니다.";
    BoardRequest reqData = new BoardRequest(null, title, content);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData)))
            .andExpect(status().isCreated())
            .andReturn();

    // then
    Board resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8), Board.class);
    Board checkData = boardRepository.findById(resData.getId()).orElse(null);

    assertNotNull(checkData);
    assertTrue(checkData.getTitle().contains(title));
    assertTrue(checkData.getContent().contains(content));
  }

  @Test
  @DisplayName("게시글 수정")
  void updateBoardTest() throws Exception {
    // given
    String url = "/api/v1/board";
    int id = 1;
    String title = "(수정) 게시글 수정 테스트의 제목";
    String content = "수정된 내용입니다.";
    BoardRequest reqData = new BoardRequest(id, title, content);
    Board beforeData = boardRepository.findById(id).orElse(null);

    // when
    mockMvc
        .perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqData)))
        .andExpect(status().isOk())
        .andReturn();

    // then
    Board afterData = boardRepository.findById(id).orElse(null);
    assertNotNull(afterData);

    assertNotNull(beforeData);
    assertNotEquals(beforeData.getTitle(), afterData.getTitle());
    assertNotEquals(beforeData.getContent(), afterData.getContent());
    assertNotEquals(beforeData.getUpdatedAt(), afterData.getUpdatedAt());
  }

  @Test
  @DisplayName("게시글 삭제")
  void deleteBoardTest() throws Exception {
    // given
    int id = 5;
    String url = "/api/v1/board/" + id;

    // when
    mockMvc.perform(delete(url)).andExpect(status().isNoContent()).andReturn();

    // then
    Board checkData = boardRepository.findById(id).orElse(null);
    assertNull(checkData);
  }
}
