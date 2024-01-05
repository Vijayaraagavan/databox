package com.vijay.databox.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.vijay.databox.core.model.SideTab;
import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.service.UserService;
import com.vijay.databox.views.modules.SideNav;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
	public String home(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        boolean logged = session.getAttribute("login") != null && (boolean) session.getAttribute("login");
        // if (logged) {
        //     return "redirect:/home";
        // }
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
        UserJwtDetails details = getDetails();
        User user = userService.getUserByUsername(details.getUsername());
        // User user = new User("vijays", "23422", "vijay@ardhika.com");
        SideTab[] tabs = new SideNav().getNav();
        PageContext ctx = new PageContext(tabs[0].id());
        model.addAttribute("user", user);
        model.addAttribute("sideTabs", tabs);
        model.addAttribute("ctx", ctx);
        return "components/home/home";
    }

    record SignUpForm (String id, String label, String name) {}
    record PageContext (String currentTab) {}

    UserJwtDetails getDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserJwtDetails) auth.getPrincipal();
    }
}
