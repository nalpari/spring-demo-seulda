package net.devgrr.springdemo;

//import lombok.RequiredArgsConstructor;
import net.devgrr.springdemo.board.BoardRepository;
import net.devgrr.springdemo.board.entity.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@RequiredArgsConstructor
@SpringBootTest
class SpringDemoApplicationTests {

	@Autowired
	private BoardRepository boardRepository;

	@Test
	void contextLoads() {
//		// 데이터 생성
//		Board insertBoard = new Board();
//		insertBoard.setTitle("테스트 제목 1");
//		insertBoard.setContent("내용내용내용내용 1");
//		insertBoard.setCreatedAt(LocalDateTime.now());
//		this.boardRepository.save(insertBoard);
//		Board insertBoard2 = new Board();
//		insertBoard2.setTitle("테스트 제목 2");
//		insertBoard2.setContent("내용내용내용내용 2");
//		insertBoard2.setCreatedAt(LocalDateTime.now());
//		this.boardRepository.save(insertBoard2);

		// 전체 조회
		List<Board> boardList = this.boardRepository.findAll();
		assertEquals(4, boardList.size());
		// 특정 데이터 검사
		assertEquals("테스트 제목 1", boardList.get(0).getTitle());

		// 단건 조회
		Optional<Board> board = this.boardRepository.findById(1);
		if (board.isPresent()) {
			Board b = board.get();
			assertEquals("내용내용내용내용 1", b.getContent());
		}

		// 키워드 조회
		List<Board> boardListFilter = this.boardRepository.findByContentLike("%용%");
		assertEquals("내용내용내용내용 1", boardListFilter.get(0).getContent());

		// 데이터 수정
		Optional<Board> updateBoard = this.boardRepository.findById(2);
		assertTrue(updateBoard.isPresent());
		Board ub = updateBoard.get();
		ub.setContent("내용내용수정수정");
		this.boardRepository.save(ub);

//		// 데이터 삭제
//		assertEquals(4, this.boardRepository.count());
//		Optional<Board> deleteBoard = boardRepository.findById(2);
//		assertTrue(deleteBoard.isPresent());
//		Board db = deleteBoard.get();
//		this.boardRepository.delete(db);
//		assertEquals(3, this.boardRepository.count());

	}

}
