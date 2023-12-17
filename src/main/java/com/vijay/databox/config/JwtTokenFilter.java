package com.vijay.databox.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vijay.databox.core.model.User;
import com.vijay.databox.core.model.UserJwtDetails;
import com.vijay.databox.core.model.UserRepository;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepo;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("inside filter" + header);
        String cookieToken = getCookieManually(request);
        if ("/login".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        if ((header == null || header.length() == 0 || !header.startsWith("Bearer ")) && cookieToken == null) {
            // response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // response.setContentType("application/json");
            // response.getWriter().write("Unauthorized: Bearer token is missing");
            filterChain.doFilter(request, response);
            return;
        }
        final String token = cookieToken == null ? header.split(" ")[1].trim() : cookieToken;
        if (!jwtTokenUtil.validateToken(token)) {
            System.out.println("not valid");

            filterChain.doFilter(request, response);
            return;
        }
        User user = userRepo.findByUserName(jwtTokenUtil.extractUsername(token)).orElse(null);
        UserJwtDetails userDetails = new UserJwtDetails(user.getUserName(), user.getPassword(), null);
        System.out.println("inside got" + userDetails);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    public String getCookieManually(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }
}
