package com.LibGO.Library.controller;

import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint to request password reset link
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean result = userService.processForgotPassword(email);

        if (result) {
            return ResponseEntity.ok("Password reset link has been sent to your university email.");
        } else {
            return ResponseEntity.badRequest().body("Email address not found in our records.");
        }
    }

    // Endpoint to submit the new password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        boolean result = userService.resetPassword(token, newPassword);

        if (result) {
            return ResponseEntity.ok("Your password has been successfully updated.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired reset token.");
        }
    }
}