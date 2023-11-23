package kr.ch08.entity.board;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//양방향 엔티티 관계 설정할때 @ToString exclude 속성으로 순환 참조되는 것을 막아서 StackOverflow 에러 방지
@ToString(exclude = "article")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="BoardFile")
public class FileEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int fno;
	private String oName;
	private String sName;
	
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ano")
	private ArticleEntity article;
	
}











