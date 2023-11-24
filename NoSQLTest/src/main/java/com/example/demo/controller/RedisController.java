package com.example.demo.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.RedisTemplateHelper;

@RestController
public class RedisController {
	@Autowired
	private RedisTemplateHelper helper;
	
	@PostMapping("/redis/string")
	public ResponseEntity<Object> setValue(@RequestParam(name="key", required=true) String key,
						@RequestParam(name="value", required=true) String value) {
		helper.setValue(key, value);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/redis/string/{key}")
	public String getValue(@PathVariable("key") String key) {
		return helper.getValue(key);
		
	}
	
	// List 데이터 getter/setter
	@PostMapping("/redis/list-right")
	public Long addToListFromRight(@RequestParam(name="key", required=true) String key,
									@RequestParam(name="value", required=true)String value) {
		return helper.addToListFromRight(key, value);
	}
	@PostMapping("/redis/list-left")
	public Long addToListFromLeft(@RequestParam(name="key", required=true) String key,
			@RequestParam(name="value", required=true)String value) {
		return helper.addToListFromLeft(key, value);
	}
	@GetMapping("/redis/list/{key}/{index}")
	public String getFromList(@PathVariable("key")String key,@PathVariable("index") int index) {
		return helper.getFromList(key, index);
	}
	//@GetMapping()
	public List<String> getRangeFromList(String key, int start, int end) {
		return null;
    }
	
	// Set 데이터 getter/setter
	//@PostMapping()
    public Long addToSet(String key, String... values) {
    	return null;
    }
	//@GetMapping()
    public Set<String> getFromSet(String key) {
    	return null;
    }

	// Hash 데이터 getter/setter
    //@PostMapping()
    public void addToHash(String key, String hashKey, String value) {
    	
    }
    //@GetMapping()
    public String getFromHash(String key, String hashKey) {
    	return null;
    }

}
