package com.vijay.databox.api;

import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.databox.api.response.UserResponse;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/api/users/{id}")
	public UserResponse doGet(@PathVariable Long id) {
		User user = userService.getUserById(id);
		return new UserResponse(user.getUserName(), user.getEmail());
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		return "index";
	}
}
