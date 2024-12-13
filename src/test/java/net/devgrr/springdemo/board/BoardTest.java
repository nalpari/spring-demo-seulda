package net.devgrr.springdemo.board;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.board.dto.BoardResponse;
import net.devgrr.springdemo.board.entity.Board;
import net.devgrr.springdemo.config.exception.BaseException;
import net.devgrr.springdemo.member.MemberRole;
import net.devgrr.springdemo.member.MemberService;
import net.devgrr.springdemo.member.dto.MemberRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BoardTest {

  private static final String USERID = "BoardTestUser";
  private static final String USERID2 = "TestUser2";
  private static final String PASSWORD = "123456789";
  private static final String BEARER_PREFIX = "Bearer ";
  private final String url = "/api/v1/board";
  @Autowired EntityManager em;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private BoardRepository boardRepository;
  @Autowired private BoardService boardService;
  @Autowired private MemberService memberService;
  private String accessToken;
  private String accessToken2;
  private int boardId;

  private void clear() {
    em.clear();
  }

  @BeforeEach
  public void init() throws Exception {
    // user1 insert
    memberService.insertUser(
        new MemberRequest(USERID, PASSWORD, "name", "test@test.test", MemberRole.USER.toString()));
    // user2 insert
    memberService.insertUser(
        new MemberRequest(
            USERID2, PASSWORD, "name2", "test2@test2.test2", MemberRole.USER.toString()));

    // user1 get token
    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("userId", USERID);
    loginRequest.put("password", PASSWORD);
    MvcResult result =
        mockMvc
            .perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    accessToken = result.getResponse().getHeader(accessHeader);
    // user2 get token
    Map<String, String> loginRequest2 = new HashMap<>();
    loginRequest2.put("userId", USERID2);
    loginRequest2.put("password", PASSWORD);
    MvcResult result2 =
        mockMvc
            .perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest2)))
            .andExpect(status().isOk())
            .andReturn();
    accessToken2 = result2.getResponse().getHeader(accessHeader);

    BoardRequest board = new BoardRequest(null, "테스트용 초기 게시글 제목", "테스트용 초기 게시글 내용");
    Board resBoard = boardService.insertBoard(board, USERID);
    boardId = resBoard.getId();

    clear();
  }

  @AfterEach
  public void cleanup() throws BaseException {
    memberService.deleteUser(USERID);
    memberService.deleteUser(USERID2);
    clear();
  }

  @Test
  @Transactional
  @DisplayName("게시글 목록 조회")
  @Order(3)
  void selectBoardTest() throws Exception {

    MvcResult mvcResult =
        mockMvc
            .perform(get(url).header("Authorization", BEARER_PREFIX + accessToken))
            .andExpect(status().isOk())
            .andReturn();

    List<BoardResponse> res =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsByteArray(), new TypeReference<>() {});
  }

  @Test
  @Transactional
  @DisplayName("게시글 조회")
  @Order(2)
  void selectBoardByIdTest() throws Exception {
    // when
    MvcResult mvcResult =
        mockMvc
            .perform(get(url + "/" + boardId).header("Authorization", BEARER_PREFIX + accessToken))
            .andExpect(status().isOk())
            .andReturn();

    // then
    BoardResponse resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
            BoardResponse.class);

    assertEquals(boardId, resData.id());
  }

  @Test
  @Transactional
  @DisplayName("게시글 생성")
  @Order(1)
  void insertBoardTest() throws Exception {
    // given
    String title = "게시글 생성 테스트의 제목";
    String content = "내용입니다.";
    BoardRequest reqData = new BoardRequest(null, title, content);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData))
                    .header("Authorization", BEARER_PREFIX + accessToken))
            .andExpect(status().isCreated())
            .andReturn();

    // then
    BoardResponse resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
            BoardResponse.class);
    boardId = resData.id().intValue();

    Board checkData = boardService.selectBoardById(boardId);

    assertEquals(title, checkData.getTitle());
    assertEquals(content, checkData.getContent());
  }

  @Test
  @DisplayName("게시글 수정")
  @Order(4)
  void updateBoardTest() throws Exception {
    // given
    String title = "(수정) 게시글 수정 테스트의 제목";
    String content = "수정된 내용입니다.";
    BoardRequest reqData = new BoardRequest(boardId, title, content);
    Board beforeData = boardService.selectBoardById(boardId);

    // when
    mockMvc
        .perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqData))
                .header("Authorization", BEARER_PREFIX + accessToken))
        .andExpect(status().isOk())
        .andReturn();

    // then
    Board afterData = boardService.selectBoardById(boardId);

    assertNotEquals(beforeData.getTitle(), afterData.getTitle());
    assertNotEquals(beforeData.getContent(), afterData.getContent());
    assertNotEquals(beforeData.getUpdatedAt(), afterData.getUpdatedAt());
    assertEquals(title, afterData.getTitle());
    assertEquals(content, afterData.getContent());
  }

  @Test
  @Transactional
  @DisplayName("게시글 삭제")
  @Order(7)
  void deleteBoardTest() throws Exception {

    // when
    mockMvc
        .perform(delete(url + "/" + boardId).header("Authorization", BEARER_PREFIX + accessToken))
        .andExpect(status().isNoContent())
        .andReturn();

    // then
    Board checkData = boardRepository.findById(boardId).orElse(null);
    assertNull(checkData);
  }

  @Test
  @Transactional
  @DisplayName("게시글 추천")
  @Order(5)
  void likeBoardTest() throws Exception {
    // given
    BoardRequest reqData = new BoardRequest(boardId, null, null);

    // when
    mockMvc
        .perform(
            put(url + "/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqData))
                .header("Authorization", BEARER_PREFIX + accessToken)) // user1
        .andExpect(status().isBadRequest())
        .andReturn();
    mockMvc
        .perform(
            put(url + "/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqData))
                .header("Authorization", BEARER_PREFIX + accessToken2)) // user2
        .andExpect(status().isOk())
        .andReturn();

    // then
    Board checkData = boardRepository.findById(boardId).orElse(null);
    assertNotNull(checkData);
    assertEquals(1, checkData.getLikes().size());
  }

  @Test
  @Transactional
  @DisplayName("게시글 추천 취소")
  @Order(6)
  void unlikeBoardTest() throws Exception {
    // given
    BoardRequest reqData = new BoardRequest(boardId, null, null);

    // when
    for (int i = 0; i < 2; i++) {
      mockMvc
          .perform(
              put(url + "/like")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(reqData))
                  .header("Authorization", BEARER_PREFIX + accessToken2)) // user2
          .andExpect(status().isOk())
          .andReturn();
    }

    // then
    Board checkData = boardRepository.findById(boardId).orElse(null);
    assertNotNull(checkData);
    assertEquals(0, checkData.getLikes().size());
  }
}
