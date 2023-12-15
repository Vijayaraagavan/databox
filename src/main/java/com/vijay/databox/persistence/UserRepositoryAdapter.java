package com.vijay.databox.persistence;

import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userJpaRepository.findByEmail(email);
	}
	
	@Override
	public Optional<User> findByUserName(String userName) {
		return userJpaRepository.findByUserName(userName);
	}
	@Override
	public Optional<Integer> findMaxIdentifier(Long id, String fileName) {
		return userJpaRepository.findMaxIdentifier( id,  fileName);
	}
}
