package com.vijay.databox.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.vijay.databox.core.model.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/")
	public String home(Model model) {
		return "index";
	}

    // @PostMapping("/login")
	// public String doPost(Model model) {
    //     return "home";
    // }

    @GetMapping("/signup")
	public String getSignUp(Model model) {
        List<SignUpForm> list = new ArrayList<SignUpForm>();
        list.add(new SignUpForm("username", "Username", "username"));
        list.add(new SignUpForm("email", "Email", "email"));
        list.add(new SignUpForm("password", "Password", "password"));
        list.add(new SignUpForm("repassword", "Retype Password", "repassword"));
        model.addAttribute("fields", list);
        return "signup";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        // model.addAttribute("username", "vijay");
        User user = new User("vijays", "23422", "vijay@ardhika.com");
        model.addAttribute("user", user);
        return "home";
    }

    record SignUpForm (String id, String label, String name) {}
}
