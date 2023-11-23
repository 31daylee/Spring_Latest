package kr.ch11.jwt;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import kr.ch11.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.val;

/**
 * @author chhak
 * @since 
 * @ JWT를 테스트하기 위한 Mocking 객체
 * 
 */

@Getter
public class JwtMock {
	
	private String issuer;
	private Key secretKey;
	private Date issuedDate = new Date();
	private Date expireDate = new Date(issuedDate.getTime() + Duration.ofHours(1).toMillis());
	
	@Builder
	public JwtMock(String issuer, Key secretKey, Date issuedDate, Date expireDate) {
		this.issuer = issuer;
		this.secretKey = secretKey;
		this.issuedDate = issuedDate != null ? issuedDate : this.issuedDate;
		this.expireDate = expireDate != null ? expireDate : this.expireDate;
	}
	
	public String createToken(UserEntity user) {
		
		// 클레임 생성(비공개)
		Claims claims = Jwts.claims();
		claims.put("uid", user.getUid());
		claims.put("role", user.getRole());
		
		String token = Jwts.builder()
						   .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 typ : JWT
						   .setIssuer(issuer) 			// 공개 클레임 토큰 발급자
						   .setIssuedAt(issuedDate)	 	// 공개 클레임 토큰 발행일(iat)
						   .setExpiration(expireDate) 	// 공개 클레임 토큰 만료일(exp)
			   			   .addClaims(claims)			// 비공개 클레임 추가
			   			   .signWith(secretKey, SignatureAlgorithm.HS256) // 토큰 서명
			   			   .compact();
		
		return token;
	}
	
	
}
