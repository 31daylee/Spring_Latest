package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.document.UserDocument;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {
 
	public Optional<UserDocument> findByUid(String uid);
	public Optional<UserDocument> deleteByUid(String uid);
}
