package com.vijay.databox.core.model;

import java.util.Optional;

import javax.persistence.Id;

public interface UserRepository {
	User save(User user);
	
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(String email);
	Optional<User> findByUserName(String userName);
	Optional<Integer> findMaxIdentifier(Long id, String fileName);
}
