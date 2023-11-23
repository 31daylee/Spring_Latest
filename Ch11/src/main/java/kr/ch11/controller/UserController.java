package kr.ch11.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.ch11.dto.UserRequestDTO;
import kr.ch11.entity.UserEntity;
import kr.ch11.jwt.JwtProvider;
import kr.ch11.repository.UserRepository;
import kr.ch11.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final UserRepository repository;
	
	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody UserRequestDTO dto) {
	
			log.info("dto : "+ dto);
			// Security 인증처리 // 11.15 : DB에 있는 사용자 정보를 조회해서 사용자 객체가 생성
			UsernamePasswordAuthenticationToken authenticationToken
				= new UsernamePasswordAuthenticationToken(dto.getUid(), dto.getPass());
			
			Authentication authentication = authenticationManager.authenticate(authenticationToken);  
			MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
			
			// 로그인이 성공했을 때 user Entity를 가져와서 넘겨준다. 다만 패스워드는 안된다.(11.15)
			UserEntity user = userDetails.getUser();
			user.setPass(null); // 패스워드도 같이 localStorage에 남기때문에, 개인정보 문제로 null로 초기화 
			
			// 토큰발행
			String accessToken = jwtProvider.createToken(user, 3);  // 3분
			String refreshToken = jwtProvider.createToken(user, 10); // 10분
			
			// user 가 Entity 이므로 String 타입이 아님. 그래서 에러가 뜨기에, Object 타입으로 변경(11.15)
			Map<String, Object> resultMap = Map.of("grantType", "Bearer", 
												   "accessToken", accessToken,
												   "refreshToken", refreshToken,
												   "user",user);
			
			return resultMap;
			
			/*
			 * }catch (Exception e) { Map<String, String> resultMap = Map.of("grantType",
			 * "None", "message", e.getMessage());
			 * 
			 * return resultMap; }
			 */
	}
	@GetMapping("/users")
	public ResponseEntity<Object> getUsers(Authentication authentication) {
		if(authentication !=null && authentication.isAuthenticated()) {
			List<UserEntity> users = repository.findAll();
			return ResponseEntity.status(HttpStatus.OK).body(users);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
		}
		
	
	}
	
}
