package com.vijay.databox.core.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vijay.databox.api.request.SignInRequest;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserRegister;
import com.vijay.databox.core.model.UserRepository;
import com.vijay.databox.exception.CustomException;
import com.vijay.databox.exception.UserExistException;
import com.vijay.databox.exception.UserValidationException;
import com.vijay.databox.validation.UserValidation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public User getUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
	}

	public User register(UserRegister request) {
		System.out.println(request);
		Map<String, String> errors = new HashMap<>();
		
		if (!UserValidation.password(request.password())) {
			// throw new CustomException("Password length must be atleast 4");
			errors.put("password", "Password length must be atleast 4");
		}
		if (!UserValidation.email(request.email())) {
			// throw new CustomException("Invalid email");
			errors.put("email", "Invalid email");
		}
		if (!UserValidation.userLength(request.username())) {
			// throw new CustomException("User name length must be atleast 3");
			errors.put("username", "User name length must be atleast 3");

		}
		if (!(request.password().equals(request.rePassword()))) {
			errors.put("repassword", "password must be same");
		}
		if (!errors.isEmpty()) {
			throw new UserValidationException("invalid", errors);
		}
		Optional<User> exist = userRepo.findByUserName(request.username());
		exist.ifPresent(user -> {
			if (user != null) {
				throw new UserExistException("User already exist");
			}
		});
		// System.out.println("found usesr " + exist.get().getUserName());
		User user = new User(request.username(), hash(request.password()), request.email());
		return userRepo.save(user);
	}

	public User login(SignInRequest request) {
		Optional<User> exist = userRepo.findByUserName(request.username());
		exist.ifPresent(user -> {
			if (!authenticate(request.password(), user.getPassword())) {
								throw new CustomException("Invalid login");
			}
		});
		return exist.orElseThrow(() -> new CustomException("user not found"));
	}

	public String hash(String password) {
		return bCryptPasswordEncoder.encode(password);
	}

	public boolean authenticate(String plain, String hashed) {
		return bCryptPasswordEncoder.matches(plain, hashed);
	}
}
