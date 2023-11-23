package kr.ch07.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = MainController.class)
public class MainControllerTest {

	/*
	 * MockMvc는 웹 어플리케이션을 서버에 배포하지 않고 
	 * Spring MVC의 동작을 재현하여 테스트 할 수 있는 클래스
	 */
	// MVC 테스트를 위한 가상 mvc 객체	
	@Autowired
	private MockMvc mvc;
	
	@BeforeAll
	public static void beforeAll() {
		System.out.println("beforeAll ...");
	}
	
	@BeforeEach
	public void beforeEach() {
		System.out.println("beforeEach ...");
	}
	
	@AfterAll
	public static void AfterAll() {
		System.out.println("AfterAll ...");
	}
	
	@AfterEach
	public void AfterEach() {
		System.out.println("AfterEach ...");
	}
	
	
	public void index() throws Exception {
		mvc
		.perform(get("/index"))				// index 요청 테스트
		.andExpect(status().isOk())			// 요청 결과 상태 테스트
		.andExpect(view().name("/index"))	// 반환되는 View 이름 테스트
		.andDo(print());					// 요청 테스트 결과 출력
	}
	
	
	public void include() throws Exception {
		
		for(int i=0 ; i<1000 ; i++) {
		
			mvc
			.perform(
				get("/include")
				.param("name", "홍길동")
				.param("age", "23")
			)		
			.andExpect(status().isOk())
			.andExpect(view().name("/include"))
			.andDo(print());
		}
	}
	
	
	public void layout() {
		System.out.println("layout ...");
	}
	
	@Test
	public void postTest() throws Exception {
		
		mvc
		.perform(
			post("/post")
			.param("uid", "p101")
			.param("name", "김유신")
		)		
		.andExpect(status().is3xxRedirection())
		.andDo(print());
		
	}
	
}
