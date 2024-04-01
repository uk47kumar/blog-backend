package com.backend.blog.service;

import com.backend.blog.payload.LoginDto;
import com.backend.blog.payload.RegisterDto;

public interface AuthService {

    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);
}
