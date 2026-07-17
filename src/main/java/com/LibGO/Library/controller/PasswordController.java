package com.LibGO.Library.controller;

import com.LibGO.Library.dto.ChangePasswordRequest;
import com.LibGO.Library.dto.ForgetPassword;
import com.LibGO.Library.dto.ResetPasswordRequest;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private UserService userService; // Switched to the updated UserService

    // Retaining your existing change password routing if needed
    @PutMapping("/change")
    public ResponseEntity<?> changePassword(@RequestParam String email, @RequestBody ChangePasswordRequest request){
        return ResponseEntity.status(501).body("Pass-through to your existing change logic if maintained.");
    }

    // 1. Request a Reset Link (Triggers Resend SMTP)
    @PostMapping("/forgot")
    public ResponseEntity<?> requestReset(@RequestBody ForgetPassword request){
        boolean linkSent = userService.processForgotPassword(request.getEmail());
        if (linkSent) {
            return ResponseEntity.ok("A secure password reset link has been dispatched via Resend SMTP.");
        } else {
            return ResponseEntity.badRequest().body("The provided email address does not exist in our records.");
        }
    }

    // 2. Submit New Password using the Token
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request){
        // Assuming request.getNewPassword() matches your ResetPasswordRequest DTO structure
        boolean resetSuccessful = userService.resetPassword(token, request.getNewPassword());
        if (resetSuccessful) {
            return ResponseEntity.ok("Your password has been successfully updated.");
        } else {
            return ResponseEntity.badRequest().body("The reset token is either invalid or has expired.");
        }
    }
}