package com.vijay.databox.persistence;

import java.util.Optional;

import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vijay.databox.core.model.User;

public interface UserJpaRepository extends JpaRepository<User, Id> {
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(String email);
}
