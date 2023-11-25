package com.vijay.databox.core.service;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	public User getUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
