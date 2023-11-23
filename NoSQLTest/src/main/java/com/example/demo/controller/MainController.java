package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.document.UserDocument;
import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;

@RestController
public class MainController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/user")
	public ResponseEntity<List<UserDocument>> findUser() {
		List<UserDocument> users = service.findAllUser();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(users);
	}
	@GetMapping("/user/{uid}")
	public void findUser(@PathVariable("uid") String uid) {
		
	}
	@PostMapping("/user")
	public ResponseEntity<UserDocument> insertUser(UserDocument user) {
		UserDocument resultUser = service.insertUser(user);
		return ResponseEntity.ok().body(resultUser);
	}
	@PutMapping("/user")
	public void updateUser(UserDocument user) {
		
	}
	@DeleteMapping("/user/{uid}")
	public void deleteUser(@PathVariable("uid") String uid) {
		
	}
	

}
