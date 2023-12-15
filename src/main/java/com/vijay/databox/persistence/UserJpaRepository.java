package com.vijay.databox.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vijay.databox.core.model.User;


public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findById(Long id);
	
	Optional<User> findByEmail(String email);

	Optional<User> findByUserName(String userName);

	@Query(value = "select max(identifier) from gallery_images where user_id = ?1 and name = ?2", nativeQuery = true)
	Optional<Integer> findMaxIdentifier(Long id, String fileName);
}
