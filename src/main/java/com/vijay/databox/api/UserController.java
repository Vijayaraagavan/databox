package com.vijay.databox.api;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.databox.api.request.SignInRequest;
import com.vijay.databox.api.request.SignUpRequest;
import com.vijay.databox.api.response.UserResponse;
import com.vijay.databox.config.JwtTokenUtil;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRegister;
import com.vijay.databox.core.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final String BASE = "/api/users";
	@Autowired
	private UserService userService;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping("/api/users/{id}")
	public UserResponse doGet(@PathVariable Long id) {
		User user = userService.getUserById(id);
		return new UserResponse(user.getUserName(), user.getEmail());
	}

	@PostMapping("/api/users")
	public UserResponse doPost(@RequestBody SignUpRequest userRequest) {
		System.out.println(userRequest);
		if (userRequest.user() == null) {
			throw new IllegalArgumentException();
		}
		UserRegister userform = new UserRegister(userRequest.user().username(), userRequest.user().email(),
				userRequest.user().password(), userRequest.user().rePassword());

		User user = userService.register(userform);
		return new UserResponse(user.getUserName(), user.getEmail());
	}

	@PostMapping("/api/login")
	// public UserResponse login(@RequestBody SignInRequest request) {
	public ResponseEntity<UserResponse> login(HttpServletRequest httpRequest, @RequestBody SignInRequest request) {
		// org.springframework.security.core.Authentication auth =
		// SecurityContextHolder.getContext().getAuthentication();
		// AuthenticationManager
		UserJwtDetails user2 = new UserJwtDetails(request.username(), request.password(), null);
		// Authentication auth2 = authenticationManager.authenticate(new
		// UsernamePasswordAuthenticationToken(user2, null));
		// System.out.println("new usr 2" + auth2);

		// Object p = auth.getPrincipal();
		// if (p instanceof String) {
		// System.out.println("new usr" + (String) p);
		// } else if (p instanceof UserJwtDetails) {
		// UserJwtDetails user1 = (UserJwtDetails) auth.getPrincipal();
		// System.out.println("new usr" + user1);

		// }
		final String token = jwtTokenUtil.generateToken(user2);
		User user = userService.login(request);
		UserResponse resp = new UserResponse(user.getUserName(), user.getEmail());
		HttpSession session =  httpRequest.getSession();
		session.setAttribute("login", true);
		return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).body(resp);
	}
	@PostMapping("/api/logout")
	// public UserResponse login(@RequestBody SignInRequest request) {
	public ResponseEntity<String> logout(HttpServletRequest httpRequest) {
		HttpSession session =  httpRequest.getSession();
		session.setAttribute("login", false);
		return ResponseEntity.ok().body("Logout successfull");
	}
}
