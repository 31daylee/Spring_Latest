package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.document.UserDocument;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	public Optional<UserDocument> findUser(String uid) {
		return userRepository.findByUid(uid);
	}
	public List<UserDocument> findAllUser() {
		return userRepository.findAll();
	}
	public UserDocument insertUser(UserDocument user) {
		return userRepository.insert(user);
	}
	public UserDocument updateUser(UserDocument user) {
		return userRepository.save(user);
	}
	public Optional<UserDocument> deleteUser(String uid) {
		return userRepository.deleteByUid(uid);
	}
	
}
