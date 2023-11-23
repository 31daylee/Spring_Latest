package kr.ch11.jwt;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import kr.ch11.entity.UserEntity;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Component
public class JwtProvider {
	/*
	 * 반드시 jjwt 라이브러리를 0.11.5 버전으로 할것
	 * 최신버전 0.12.x(심지어 2023년 10월 5일 등록됨) 로 하면 안됨!
	 * (메서드가 바뀌고 상당히 많은 내용이 Deprecated 됨)
	 */
	private String issuer;
	private SecretKey secretKey;
	
	public JwtProvider(@Value("${jwt.issuer}") String issuer,
					   @Value("${jwt.secret}") String secret) {
		
		this.issuer = issuer;
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String createToken(UserEntity user, int days) {
		
		// 발급일, 만료일 생성
		Date issuedDate = new Date();
		Date expireDate = new Date(issuedDate.getTime() + Duration.ofMinutes(days).toMillis());
		
		// 클레임 생성
		Claims claims = Jwts.claims();
		claims.put("uid", user.getUid());
		claims.put("role", user.getRole());
		
		// 토큰 생성
		String token = Jwts.builder()
						   .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
						   .setIssuer(issuer)
						   .setIssuedAt(issuedDate)
						   .setExpiration(expireDate)
						   .addClaims(claims)
						   .signWith(secretKey, SignatureAlgorithm.HS256)
						   .compact();
		
		return token;
	}
	
	public Authentication getAuthentication(String token) {
		
		Claims claims = getClaims(token);
		String uid  = (String) claims.get("uid");
		String role = (String) claims.get("role");
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_"+role)); 
		
		User principal = new User(uid, "", authorities);
		
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
	
	public boolean validateToken(String token) {
		
		try {
			
			Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(token);
			
			return true;
			
		}catch (SecurityException | MalformedJwtException e) {
			log.debug("잘못된 JWT 서명입니다.");
			throw new MalformedJwtException("MalformedJwtException", e);
		}catch (ExpiredJwtException e) {
			log.debug("만료된 JWT 토큰입니다.");
			throw new ExpiredJwtException(null, null, "ExpiredJwtException",e);
		}catch (UnsupportedJwtException e) {
			log.debug("지원되지 않는 JWT 토큰입니다.");
			throw new UnsupportedJwtException("UnsupportedJwtException",e);
		}catch (IllegalArgumentException e) {
			log.debug("JWT 토큰이 잘못되었습니다.");
			throw new IllegalArgumentException("IllegalAccessException",e);
		}
		
		//return false;
	}
	
	public Claims getClaims(String token){
		return Jwts.parserBuilder()
				   .setSigningKey(secretKey)
				   .build()
				   .parseClaimsJws(token)
				   .getBody();
	}

}
