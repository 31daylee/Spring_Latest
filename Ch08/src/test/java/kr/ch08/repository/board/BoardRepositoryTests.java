package kr.ch08.repository.board;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.transaction.Transactional;
import kr.ch08.entity.board.ArticleEntity;
import kr.ch08.entity.board.CommentEntity;
import kr.ch08.entity.board.FileEntity;
import kr.ch08.entity.board.UserEntity;

@SpringBootTest
public class BoardRepositoryTests {
	
	@Autowired private ArticleRepository articleRepo;
	@Autowired private CommentRepository commentRepo;
	@Autowired private FileRepository fileRepo;
	@Autowired private UserRepository userRepo;
	
	
	
	// 작업1 - 사용자 등록
	@DisplayName("insertUser 테스트...")
	public void insertUser() {
		
		UserEntity user = UserEntity.builder()
							.uid("a103")
							.name("장보고")
							.hp("010-1234-1003")
							.build();
		
		userRepo.save(user);
	}

	// 작업2 - 글 등록
	@DisplayName("insertArticle 테스트...")
	public void insertArticle() {
		
		UserEntity user = UserEntity.builder().uid("a103").build();
		
		ArticleEntity article = ArticleEntity.builder()
								.title("제목1 입니다.")
								.content("내용1 입니다.")
								.user(user)
								.build();
		
		articleRepo.save(article);
	}
	
	// 작업3 - 댓글 등록
	@DisplayName("insertComment 테스트...")
	public void insertComment() {
		
		UserEntity user = UserEntity.builder().uid("a102").build();
		
		ArticleEntity article = ArticleEntity.builder()
								.no(5)
								.build();
		
		CommentEntity comment = CommentEntity.builder()
								.content("댓글1 입니다.")
								.user(user)
								.article(article)
								.build();
		
		commentRepo.save(comment);
	}
	
	// 작업4 - 파일 등록
	@DisplayName("insertFile 테스트...")
	public void insertFile() {
		
		ArticleEntity article = ArticleEntity.builder().no(2).build();
		FileEntity file = FileEntity.builder()
							.oName("테스트2.txt")
							.sName("afnkefaeiXXX.txt")
							.article(article)
							.build();
		
		fileRepo.save(file);
	}
	
	
	@DisplayName("selectArticles 테스트...")
	public void selectArticles() {
		List<ArticleEntity> articles = articleRepo.findAll();
		System.out.println(articles);
		
		for(ArticleEntity article : articles) {
			//System.out.println(article);
		}
	}
	
	
	@Test
	@Transactional
	/*
	 * 양방향으로 처리되는 엔티티 관계에서 SELECT를 두번 처리하기 때문에
	 * 첫번째 SELECT를 처리하고 데이터베이스와 세션이 끝나기 때문에 
	 * 두번째 SELECT를 실행할 때는 no session 에러가 발생
	 * 이럴경우 @Transactional 선언으로 트랜잭션 처리를 해줘야 됨 
	 */
	@DisplayName("selectArticle 테스트...")
	public void selectArticle() {
		Optional<ArticleEntity> result = articleRepo.findById(1);
		ArticleEntity article = result.orElseThrow();
		
		System.out.println(article);
	}
}



























