package com.example.supabaseauth.controller;

import com.example.supabaseauth.dto.LoginRequest;
import com.example.supabaseauth.dto.SignupRequest;
import com.example.supabaseauth.dto.ProfileResponse;
import com.example.supabaseauth.service.SupabaseAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SupabaseAuthService service;

    public AuthController(SupabaseAuthService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"error\":\"email and password required\"}"); 
        }
        String resp = service.signup(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {
        if (req.getEmail() == null || req.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"error\":\"email and password required\"}"); 
        }
        String resp = service.login(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> profile(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        ProfileResponse profile = service.profile(auth);
        return ResponseEntity.ok(profile);
    }
}
