package kr.ch07.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class User6Controller {

	@GetMapping("/user6/register")
	public String register() {
		return "/user6/register";
	}
	
	
	@GetMapping("/user6/list")
	public String list() {
		return "/user6/list";
	}
}
