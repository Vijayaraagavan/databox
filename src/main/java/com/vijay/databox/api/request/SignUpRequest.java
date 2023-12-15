package com.vijay.databox.api.request;

public record SignUpRequest(Params user) {
    public record Params(String username, String email, String password, String rePassword) {}
}   
