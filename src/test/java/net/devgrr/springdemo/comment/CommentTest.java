package net.devgrr.springdemo.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.devgrr.springdemo.board.BoardService;
import net.devgrr.springdemo.board.dto.BoardRequest;
import net.devgrr.springdemo.comment.dto.CommentRequest;
import net.devgrr.springdemo.comment.dto.CommentResponse;
import net.devgrr.springdemo.comment.entity.Comment;
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
import org.springframework.transaction.annotation.Transactional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CommentTest {

  private static final String USERID = "user1";
  private static final String USERID2 = "user2";
  private static final String PASSWORD = "password";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String COMMENT_URL = "/api/v1/comment";
  @Autowired EntityManager em;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Autowired private MockMvc mockMvc;
  @Autowired private BoardService boardService;
  @Autowired private CommentService commentService;
  @Autowired private ObjectMapper objectMapper;

  private String accessToken;
  private String accessToken2;
  private int boardId;
  private int commentId;
  @Autowired private CommentRepository commentRepository;

  private void clear() {
    em.clear();
  }

  private String getAccessToken(String userId, String password) throws Exception {
    Map<String, String> loginRequest = new HashMap<>();
    loginRequest.put("userId", userId);
    loginRequest.put("password", password);
    MvcResult result =
        mockMvc
            .perform(
                post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    return result.getResponse().getHeader(accessHeader);
  }

  @BeforeEach
  public void init() throws Exception {
    // user1 get token
    accessToken = getAccessToken(USERID, PASSWORD);
    // user2 get token
    accessToken2 = getAccessToken(USERID2, PASSWORD);

    // 게시글 생성 by user1
    BoardRequest board = new BoardRequest(null, "테스트용 초기 게시글 제목", "테스트용 초기 게시글 내용", null);
    boardId = boardService.insertBoard(board, USERID).getId();
    // 댓글 생성 by user1
    CommentRequest commentRequest = new CommentRequest(boardId, null, null, "초기 댓글 내용1");
    commentId = commentService.insertComment(commentRequest, USERID).getId();
    commentService.insertComment(new CommentRequest(boardId, null, null, "초기 댓글 내용2"), USERID2);

    clear();
  }

  @AfterEach
  public void cleanup() {
    clear();
  }

  @Test
  @Transactional
  @DisplayName("댓글 목록 조회")
  @Order(3)
  void getCommentsTest() throws Exception {
    // given
    String requestUrl = COMMENT_URL + "/" + boardId;

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(get(requestUrl).header(accessHeader, BEARER_PREFIX + accessToken))
            .andReturn();

    // then
    List<CommentResponse> resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
            objectMapper
                .getTypeFactory()
                .constructCollectionType(List.class, CommentResponse.class));

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals(2, resData.size());
  }

  @Test
  @Transactional
  @DisplayName("댓글 생성")
  @Order(1)
  void createCommentTest() throws Exception {
    // given
    String content = "댓글 생성 테스트의 내용";
    CommentRequest reqData = new CommentRequest(boardId, null, null, content);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(COMMENT_URL)
                    .header(accessHeader, BEARER_PREFIX + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData)))
            .andReturn();

    // then
    CommentResponse resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
            CommentResponse.class);

    assertEquals(201, mvcResult.getResponse().getStatus());
    assertEquals(boardId, resData.boardId());
    assertEquals(content, resData.content());
  }

  @Test
  @Transactional
  @DisplayName("대댓글 생성")
  @Order(2)
  void createChildCommentTest() throws Exception {
    // given
    String content = "대댓글 생성 테스트의 내용";
    CommentRequest reqData = new CommentRequest(boardId, null, commentId, content);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(
                post(COMMENT_URL)
                    .header(accessHeader, BEARER_PREFIX + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData)))
            .andReturn();

    // then
    CommentResponse resData =
        objectMapper.readValue(
            mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8),
            CommentResponse.class);

    assertEquals(201, mvcResult.getResponse().getStatus());
    assertEquals(boardId, resData.boardId());
    assertEquals(commentId, resData.parentCommentId());
    assertEquals(content, resData.content());
  }

  @Test
  @Transactional
  @DisplayName("댓글 수정")
  @Order(4)
  void updateCommentTest() throws Exception {
    // given
    String content = "수정된 댓글 내용";
    CommentRequest reqData = new CommentRequest(boardId, commentId, null, content);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(
                put(COMMENT_URL)
                    .header(accessHeader, BEARER_PREFIX + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData)))
            .andReturn();

    // then
    Comment checkData = commentService.selectCommentById(commentId);

    assertEquals(200, mvcResult.getResponse().getStatus());
    assertEquals(boardId, checkData.getBoard().getId());
    assertEquals(commentId, checkData.getId());
    assertEquals(content, checkData.getContent());
  }

  @Test
  @Transactional
  @DisplayName("댓글 삭제")
  @Order(7)
  void deleteCommentTest() throws Exception {
    // given
    String requestUrl = COMMENT_URL + "/" + commentId;

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(delete(requestUrl).header(accessHeader, BEARER_PREFIX + accessToken))
            .andReturn();

    // then
    Comment checkData = commentRepository.findById(commentId).orElse(null);

    assertEquals(204, mvcResult.getResponse().getStatus());
    assertNull(checkData);
  }

  @Test
  @Transactional
  @DisplayName("댓글 추천")
  @Order(5)
  void likeCommentTest() throws Exception {
    // given
    String requestUrl = COMMENT_URL + "/like";
    CommentRequest reqData = new CommentRequest(null, commentId, null, null);

    // when
    MvcResult mvcResult1 =
        mockMvc
            .perform(
                put(requestUrl)
                    .header(accessHeader, BEARER_PREFIX + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData))) // user1
            .andReturn();
    MvcResult mvcResult2 =
        mockMvc
            .perform(
                put(requestUrl)
                    .header(accessHeader, BEARER_PREFIX + accessToken2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData))) // user2
            .andReturn();

    // then
    Comment checkData = commentService.selectCommentById(commentId);

    assertEquals(400, mvcResult1.getResponse().getStatus());
    assertEquals(200, mvcResult2.getResponse().getStatus());
    assertEquals(1, checkData.getLikes().size());
  }

  @Test
  @Transactional
  @DisplayName("댓글 추천 취소")
  @Order(6)
  void unlikeCommentTest() throws Exception {
    // given
    String requestUrl = COMMENT_URL + "/like";
    CommentRequest reqData = new CommentRequest(null, commentId, null, null);

    // when
    MvcResult mvcResult1 =
        mockMvc
            .perform(
                put(requestUrl)
                    .header(accessHeader, BEARER_PREFIX + accessToken2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData))) // user2
            .andReturn();
    MvcResult mvcResult2 =
        mockMvc
            .perform(
                put(requestUrl)
                    .header(accessHeader, BEARER_PREFIX + accessToken2)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqData))) // user2
            .andReturn();

    // then
    Comment checkData = commentService.selectCommentById(commentId);
    assertEquals(200, mvcResult1.getResponse().getStatus());
    assertEquals(200, mvcResult2.getResponse().getStatus());
    assertEquals(0, checkData.getLikes().size());
  }
}
