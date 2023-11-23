package kr.ch10.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.ch10.entity.User1Entity;
import kr.ch10.service.User1Service;

@CrossOrigin("http://localhost:5173")
@Controller
public class User1Controller {

	@Autowired
	private User1Service service;
	
	@ResponseBody
	@GetMapping("/user1")
	public List<User1Entity> list() {
		List<User1Entity> user1s = service.selectUser1s();
		return user1s;
	}
	
	@ResponseBody
	@GetMapping("/user1/{id}")
	public User1Entity user1(@PathVariable("id") String id) {
		return service.selectUser1(id);
	}

	//@RequestBody ajax로 전송되는 데이터 수신
	@ResponseBody
	@PostMapping("/user1")
	public void register(@RequestBody User1Entity user1) {
		service.insertUser1(user1);
	}
	
	@ResponseBody
	@PutMapping("/user1")
	public void modify(@RequestBody User1Entity user1) {
		service.updateUser1(user1);
	}
	
	@ResponseBody
	@DeleteMapping("/user1/{id}")
	public void delete(@PathVariable("id") String id) {
		service.deleteUser1(id);
	}
}
