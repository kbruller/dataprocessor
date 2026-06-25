package com.example.dataprocessor.controller;

import com.example.dataprocessor.model.dto.LoginRequest;
import com.example.dataprocessor.model.dto.LoginResponse;
import com.example.dataprocessor.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            JwtService jwtService,
            AuthenticationManager authenticationManager) {

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.username(),
                                request.password()
                        )
                );

        String role = authentication
                .getAuthorities()
                .stream()
                .filter(a -> a.getAuthority().startsWith("ROLE_"))
                .findFirst()
                .orElseThrow()
                .getAuthority();

        String token =
                jwtService.generateToken(
                        request.username(),
                        role
                );

        System.out.println(
                jwtService.extractUsername(token)
        );

        return new LoginResponse(token);
    }
}
