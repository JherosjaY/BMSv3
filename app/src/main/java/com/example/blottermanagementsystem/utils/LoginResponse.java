package com.example.blottermanagementsystem.utils;

import com.example.blottermanagementsystem.data.entity.User;

public class LoginResponse {
    public boolean success;
    public String message;
    public LoginData data;
    
    public static class LoginData {
        public User user;
        public String token;
    }
}
