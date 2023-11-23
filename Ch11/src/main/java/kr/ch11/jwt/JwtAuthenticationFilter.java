package kr.ch11.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.ch11.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	
	public static final String AUTH_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String uri = request.getRequestURI(); // -> /Ch11/refreshToken
		int i = uri.lastIndexOf("/");
		String path = uri.substring(i); // -> refreshToken 
		log.info("JwtAuthenticationFilter...1");
		
		String header = request.getHeader(AUTH_HEADER);
		log.info("JwtAuthenticationFilter...2 : " + header);
		
		String token = getTokenFromHeader(header);
		log.info("JwtAuthenticationFilter...3 : " + token);
		
		if(token != null) {
			try {
				jwtProvider.validateToken(token); // 토큰이 성공하면 Authentication~ 인증 처리를 받고 아니면 return 으로 예외를 던져버림
				
				// refresh 토큰 검사가 성공이면
				if(path.equals("/refreshToken")) {
					
					Claims claims = jwtProvider.getClaims(token);
					String uid  = (String) claims.get("uid");
					String role = (String) claims.get("role");
					
					UserEntity user = UserEntity.builder()
							.uid(uid)
							.role(role).build();

					String accessToken = jwtProvider.createToken(user, 3);
					
					response.setStatus(HttpServletResponse.SC_CREATED);
					response.getWriter().print(accessToken);
					return;
				}
			}
			catch (SecurityException | MalformedJwtException e) {
				log.debug("잘못된 JWT 서명입니다.");
				response.setStatus(401);
				response.getWriter().print("잘못된 JWT 서명입니다. ");
				return;
			}catch (ExpiredJwtException e) {
				log.debug("만료된 JWT 토큰입니다.");
				response.setStatus(402);
				response.getWriter().print("만료된 JWT 서명입니다. ");
				return;
			}catch (UnsupportedJwtException e) {
				log.debug("지원되지 않는 JWT 토큰입니다.");
				response.setStatus(403);
				response.getWriter().print("지원되지 않는 JWT 토큰입니다. ");
				return;
			}catch (IllegalArgumentException e) {
				log.debug("JWT 토큰이 잘못되었습니다.");
				response.setStatus(404);
				response.getWriter().print("JWT 토큰이 잘못되었습니다. "); // 예외처리를 위한 부분. 원래는 하나의 personal exception 으로 던져줘야하는데 시간이 없어서 여기서 처리
				return;
			}
			log.info("JwtAuthenticationFilter...4");
			// Security 인증처리
			Authentication authentication = jwtProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		// 다음 필터 이동
		filterChain.doFilter(request, response);
	}

	public String getTokenFromHeader(String header) {
		
		if(header != null && header.startsWith(TOKEN_PREFIX)) {
			return header.substring(TOKEN_PREFIX.length());
		}
		
		return null;
	}
	
}








