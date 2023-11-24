package com.example.demo.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
public class RedisTemplateHelper {
	
	/*
	 * RedisTemplate 설정
	 *  - RedisConfig에서 RedisTemplate 설정을 해야 됨
	 */
    private final StringRedisTemplate redisTemplate;

	// String 데이터 getter/setter
	public void setValue(String key, String value) {
		ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
		valueOps.set(key, value);
	}
	
	public String getValue(String key) {
		ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
	}
	
	// List 데이터 getter/setter
	public Long addToListFromRight(String key, String value) {
		ListOperations<String, String> listOps = redisTemplate.opsForList();
		return listOps.rightPush(key, value);
	}
	
	public Long addToListFromLeft(String key, String value) {
		ListOperations<String, String> listOps = redisTemplate.opsForList();
		return listOps.leftPush(key, value);
	}
	
	public String getFromList(String key, int index) {
		ListOperations<String, String> listOps = redisTemplate.opsForList();
        return listOps.index(key, index);
	}
	
	public List<String> getRangeFromList(String key, int start, int end) {
        ListOperations<String, String> listOps = redisTemplate.opsForList();
        return listOps.range(key, start, end);
    }
	
	// Set 데이터 getter/setter
    public Long addToSet(String key, String... values) {
    	SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.add(key, values);
    }

    public Set<String> getFromSet(String key) {
    	SetOperations<String, String> setOps = redisTemplate.opsForSet();
        return setOps.members(key);
    }

	// Hash 데이터 getter/setter
    public void addToHash(String key, String hashKey, String value) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        hashOps.put(key, hashKey, value);
    }

    public String getFromHash(String key, String hashKey) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.get(key, hashKey);
    }
}