package com.LibGO.Library.controller;

import com.LibGO.Library.dto.ChangePasswordRequest;
import com.LibGO.Library.dto.ForgetPassword;
import com.LibGO.Library.dto.ResetPasswordRequest;
import com.LibGO.Library.exception.LibGOException;
import com.LibGO.Library.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    PasswordService passwordService;

    @PutMapping("/change")
    public ResponseEntity<?> changePassword(@RequestParam String email, @RequestBody ChangePasswordRequest request){
        try {
           return ResponseEntity.ok(passwordService.changePassword(email, request));
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> requestReset(@RequestBody ForgetPassword request){
        try {
            return ResponseEntity.ok(passwordService.sendOtp(request.getEmail()));
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request){
        try {
            return ResponseEntity.ok(passwordService.resetPassword(request));
        }
        catch (LibGOException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
