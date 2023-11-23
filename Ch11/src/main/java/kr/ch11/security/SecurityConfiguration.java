package kr.ch11.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import kr.ch11.jwt.JwtAuthenticationFilter;
import kr.ch11.jwt.JwtProvider;

@Configuration
public class SecurityConfiguration {

	@Autowired
	private SecurityUserService service;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			// 사이트 위변조 방지 비활성
			.csrf(CsrfConfigurer::disable) // 메서드 참조 연산자로 람다식을 간결하게 표현
			.cors(cust->cust.configurationSource(
					req->{
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
						config.setAllowedMethods(Collections.singletonList("*"));
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;
					}))
			// 기본 HTTP 인증방식 비활성
			.httpBasic(HttpBasicConfigurer::disable)
			// 토큰방식으로 로그인 처리하기 때문에 폼방식 비활성
			.formLogin(FormLoginConfigurer::disable)
			// 토큰기반 인증 방식이기 때문에 세션을 사용안함
			.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			// 토큰 검사 필터 설정
			.addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
			// 인가 권한 설정
			.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
									.requestMatchers("/admin/**").hasAuthority("ADMIN")
									.requestMatchers("/manager/**").hasAnyAuthority("ADMIN", "MANAGER")
									.requestMatchers("/user/**").permitAll()
									.anyRequest().permitAll());		
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	// Spring Security에서 CORS 해제 필터 - 강사님 ver.
	/*
	 * @Bean public CorsFilter corsFilter() { UrlBasedCorsConfigurationSource source
	 * = new UrlBasedCorsConfigurationSource(); CorsConfiguration config = new
	 * CorsConfiguration(); config.addAllowedOrigin("http://localhost:5173"); // <--
	 * 반드시 허용 도메인 주소를 작성, *(전체) 허용하면 안됨 config.addAllowedMethod("*");
	 * config.addAllowedHeader("*"); config.setAllowCredentials(true);
	 * source.registerCorsConfiguration("/**", config); return new
	 * CorsFilter(source); }
	 */
}
