package com.vijay.databox.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.databox.core.model.User;


public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(String email);

	Optional<User> findByUserName(String userName);
}
